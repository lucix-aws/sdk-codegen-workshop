# SDK Codegen Workshop

This is the template repository for an in-person codegen workshop that walks
students through the process of generating a barebones SDK from a Smithy API
model.

## For students: Setup instructions

### 1. Install tools

This workshop requires the following tools:

- Smithy CLI
    - `brew tap smithy-lang/tap && brew install smithy-cli`
- Java 17+
    - `brew install openjdk@17`
- Node **>= v23.6.0** (for builtin TypeScript stripping)
    - `brew install node@24`

### 2. Test the runtime

To test that the runtime modules (that your generated code will reference) work
with your Node install, run the `check-runtime` script:

```sh
node check-runtime.ts
```

This script performs a basic request to Amazon SQS to test that all components
are working (and that your Node install can interpret the runtime modules).

One of two things will happen, **both of which indicate success**:

- If you don't have AWS credentials in your env vars, you'll get a 403
  (pictured below).
- If you do have AWS credentials, you'll get a 200 and see the results of a
  ListQueues request against us-east-1 in the output.

The output should look something like the following:

```
testing runtime...
environment credentials {
  "akid": "",
  "secret": "",
  "sessionToken": ""
}

signing request...

signature AWS4-HMAC-SHA256 <...>

sending request...

response status 403

The security token included in the request is invalid.
```

### 3. Test the code generator

To test the code generation and Smithy CLI, run `build.sh`.

```sh
./build.sh
```

You should see some Smithy model validation messages, followed by this:

```
----------------------------
hello from our build plugin!
you have selected the service: com.amazonaws.sqs#AmazonSQS
----------------------------
```
