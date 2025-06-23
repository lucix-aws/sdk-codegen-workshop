/**
 * This runtime module facilitates signing HTTP requests with AWS Signature Version 4.
 *
 * You can read more about AWS SigV4 and the signing algorithm at
 * https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_sigv-create-signed-request.html
 *
 * THIS IS NOT A FULL IMPLEMENTATION. The implementation cheats in the
 * canonicalization process for certain elements that aren't relevant in the
 * awsJson 1.0 protocol we're using in this workshop.
 */

import crypto from "crypto";

import type { HTTPRequest } from "./http.ts";
import type { Credentials } from "./credentials.ts";

const ALGORITHM = "AWS4-HMAC-SHA256";

/**
 * Signs the provided HTTP request in-place with SigV4.
 */
export const signRequest = (
	request: HTTPRequest,
	credentials: Credentials,
	service: string,
	region: string
) => {
	const signingTime = new Date();
	const scope = buildCredentialScope(signingTime, region, service);

	setRequiredHeaders(request, signingTime, credentials);

	const [canonicalRequest, signedHeaders] = buildCanonicalRequest(request);
	const stringToSign = buildStringToSign(
		canonicalRequest,
		signingTime,
		scope
	);
	const signature = signString(
		stringToSign,
		credentials.secret,
		region,
		service,
		signingTime
	);

	request.headers["Authorization"] = buildAuthorizationHeader(
		signature,
		signedHeaders,
		credentials.akid,
		scope
	);
};

// implementation details of sigv4...

const buildCredentialScope = (
	time: Date,
	region: string,
	service: string
): string => [shortTimeFormat(time), region, service, "aws4_request"].join("/");

const setRequiredHeaders = (
	request: HTTPRequest,
	time: Date,
	credentials: Credentials
) => {
	request.headers["Host"] = request.host;
	request.headers["X-Amz-Date"] = fullTimeFormat(time);
	if (!!credentials.sessionToken) {
		request.headers["X-Amz-Security-Token"] = credentials.sessionToken;
	}
};

// returns (canonical string, signed headers)
const buildCanonicalRequest = (request: HTTPRequest): [string, string] => {
	const cPath = "/"; // no path in awsJson10
	const cQuery = ""; // no query in awsJson10

	const [cHeaders, signedHeaders] = buildCanonicalHeaders(request.headers);

	const payloadHash = sha256(request.body ?? "", true);

	const cReq = [
		request.method,
		cPath,
		cQuery,
		cHeaders,
		signedHeaders,
		payloadHash,
	].join("\n");
	return [cReq, signedHeaders];
};

// returns (canon headers, signed headers)
const buildCanonicalHeaders = (
	headers: Record<string, string | string[]>
): [string, string] => {
	const cHeaders: string[] = [];
	const sHeaders: Record<string, string[]> = {};

	for (let [header, values] of Object.entries(headers)) {
		header = header.toLowerCase();
		if (shouldSign(header)) {
			cHeaders.push(header);
			sHeaders[header] = typeof values === "string" ? [values] : values;
		}
	}

	cHeaders.sort();

	let chStr = ""; // canonical header string
	for (const h of cHeaders) {
		chStr += h + ":";
		chStr += sHeaders[h].map((v) => v.trim()).join(",");
		chStr += "\n";
	}

	return [chStr, cHeaders.join(";")];
};

const buildStringToSign = (
	canonicalRequest: string,
	time: Date,
	scope: string
): string =>
	[
		ALGORITHM,
		fullTimeFormat(time),
		scope,
		sha256(canonicalRequest, true),
	].join("\n");

const buildAuthorizationHeader = (
	signature: string,
	headers: string,
	akid: string,
	scope: string
): string => {
	let header = `${ALGORITHM} `;
	header += `Credential=${akid}/${scope}, `;
	header += `SignedHeaders=${headers}, `;
	header += `Signature=${signature}`;
	return header;
};

const shortTimeFormat = (time: Date): string => {
	const yyyy = time.getUTCFullYear();
	const mm = pad2(time.getUTCMonth() + 1);
	const dd = pad2(time.getUTCDate());

	return `${yyyy}${mm}${dd}`;
};

const fullTimeFormat = (time: Date): string => {
	const yyyy = time.getUTCFullYear();
	const MM = pad2(time.getUTCMonth() + 1);
	const dd = pad2(time.getUTCDate());
	const hh = pad2(time.getUTCHours());
	const mm = pad2(time.getUTCMinutes());
	const ss = pad2(time.getUTCSeconds());

	return `${yyyy}${MM}${dd}T${hh}${mm}${ss}Z`;
};

const signString = (
	toSign: string,
	secret: string,
	region: string,
	service: string,
	time: Date
): string => {
	let key = hmacSHA256("AWS4" + secret, shortTimeFormat(time));
	key = hmacSHA256(key, region);
	key = hmacSHA256(key, service);
	key = hmacSHA256(key, "aws4_request");
	return crypto.createHmac("sha256", key).update(toSign).digest("hex");
};

const shouldSign = (header: string): boolean =>
	header === "host" || header.startsWith("x-amz-");

const pad2 = (i: number): string => (i < 10 ? `0${i}` : `${i}`);

const hmacSHA256 = (key: crypto.BinaryLike, data: crypto.BinaryLike): Buffer =>
	crypto.createHmac("sha256", key).update(data).digest();

const sha256 = (data: string, encodeHex?: boolean): string =>
	crypto
		.createHash("sha256")
		.update(data)
		.digest(encodeHex ? "hex" : "binary");
