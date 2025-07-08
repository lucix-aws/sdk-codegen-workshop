import { Client } from "./client.ts";

(async function () {
	const sqsClient = new Client({
		region: "us-east-1",
		credentials: {
			akid: process.env.AWS_ACCESS_KEY_ID,
			secret: process.env.AWS_SECRET_ACCESS_KEY,
			sessionToken: process.env.AWS_SESSION_TOKEN,
		},
	});

	console.log(sqsClient.options.credentials);

	const response = await sqsClient.ListQueues({});
	console.log(response);
})();
