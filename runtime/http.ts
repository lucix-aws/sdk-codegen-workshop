/**
 * This runtime module facilitates performing HTTP requests to AWS services.
 * SDKs will typically define their own domain-specific HTTP container types.
 *
 * Usually we have the concept of an HTTP "client" that implies its own
 * resources and lifecycle (e.g. a connection pool) but for this workshop we
 * are foregoing that abstraction and instead exposing a simple doRequest() API
 * that will make HTTP requests for us.
 */

import http from "http";
import https from "https";

/**
 * Enumeration of HTTP methods.
 */
export type HTTPMethod = "HEAD" | "GET" | "PUT" | "POST" | "DELETE";

/**
 * Container type for an outgoing HTTP request.
 */
export type HTTPRequest = {
	method: HTTPMethod;
	host: string;
	path: string;
	headers: Record<string, string | string[]>;
	body?: string;
};

/**
 * Container type for an outgoing HTTP response.
 */
export type HTTPResponse = {
	statusCode: number;
	headers: Record<string, string | string[]>;
	body?: string;
};

/**
 * Asynchronously perform an HTTP request, returning the response to the caller.
 */
export const doRequest = (request: HTTPRequest): Promise<HTTPResponse> =>
	new Promise((resolve, reject) => {
		const options: http.RequestOptions = {
			method: request.method,
			host: request.host,
			path: request.path,
			headers: request.headers,
		};

		let responseBody: string | undefined = undefined;
		const req = https.request(options, (res) => {
			res.on("data", (chunk) => {
				if (responseBody === undefined) {
					responseBody = "";
				}
				responseBody += chunk;
			});
			res.on("end", () => {
				resolve({
					statusCode: res.statusCode ?? -1,
					headers: mapResponseHeaders(res.headers),
					body: responseBody,
				});
			});

			res.on("error", reject);
		});

		if (request.body !== undefined) {
			req.write(request.body);
		}
		req.end();
	});

// map node http headers to our more explicit type
const mapResponseHeaders = (
	inc: http.IncomingHttpHeaders
): Record<string, string[]> => {
	const mapped: Record<string, string[]> = {};

	for (const [header, value] of Object.entries(inc)) {
		if (typeof value === "string") {
			mapped[header] = [value];
		} else if (value !== undefined) {
			mapped[header] = value;
		}
	}

	return mapped;
};
