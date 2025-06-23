// make sure our runtime is working

import type { HTTPRequest } from "./runtime/http.ts";
import type { Credentials } from "./runtime/credentials.ts";

import { signRequest } from "./runtime/sigv4.ts";
import { doRequest } from "./runtime/http.ts";

(async function () {
	console.log();
	console.log("testing runtime...");

	const requestData = {};

	const request: HTTPRequest = {
		method: "POST",
		path: "/",
		host: "sqs.us-east-1.amazonaws.com",
		headers: {
			"Content-Type": "application/x-amz-json-1.0",
			"X-Amz-Target": "AmazonSQS.ListQueues",
		},
		body: JSON.stringify(requestData),
	};

	const credentials: Credentials = {
		akid: process.env.AWS_ACCESS_KEY_ID ?? "",
		secret: process.env.AWS_SECRET_ACCESS_KEY ?? "",
		sessionToken: process.env.AWS_SESSION_TOKEN ?? "",
	};

	console.log(
		"environment credentials",
		JSON.stringify(credentials, null, 2)
	);
	console.log();

	console.log("signing request...");
	console.log();
	signRequest(request, credentials, "sqs", "us-east-1");
	console.log("signature", request.headers["Authorization"]);
	console.log();

	console.log("sending request...");
	console.log();
	const response = await doRequest(request);
	const body = JSON.parse(response.body ?? "");

	console.log("response status", response.statusCode);
	console.log();
	if (!!body.message) {
		console.log(body.message);
	} else {
		console.log("response body", body);
	}
})();
