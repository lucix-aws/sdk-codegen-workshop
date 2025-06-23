package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.ts.codegen.TypeScriptWriter;

/**
 * Generates the options structure that callers pass when creating an instance of a service client.
 * This basic SDK will support two configuration fields:
 * 1. The AWS region (a string).
 * 2. The customer credentials (an instance of the sigv4.Credentials type exposed in our runtime).
 */
public class ClientOptionsGenerator {
    public static final String STRUCTURE_NAME = "ClientOptions";

    public void render(TypeScriptWriter writer) {
        writer.openBlock("export type $L = {", "}", STRUCTURE_NAME, () -> {
            // render config fields...
        });
    }
}
