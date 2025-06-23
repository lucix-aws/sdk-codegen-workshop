/**
 * This runtime module exposes types for AWS credentials and credential
 * provision.
 */

/**
 * AWS credentials used to sign with SigV4.
 */
export type Credentials = {
	akid: string;
	secret: string;
	sessionToken?: string;
};
