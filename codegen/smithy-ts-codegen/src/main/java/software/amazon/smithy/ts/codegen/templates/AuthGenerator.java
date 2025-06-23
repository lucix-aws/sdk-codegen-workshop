package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.ServiceShape;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

/**
 * The auth generator will generate the necessary code (using the provided runtime) to sign
 * requests.
 * For this exercise we will only support sigv4.
 */
public class AuthGenerator {
    private final Model model;
    private final ServiceShape service;

    public AuthGenerator(Model model, ServiceShape service) {
        this.model = model;
        this.service = service;
    }

    // preconditions:
    // * operation generator has declared request of type HTTPRequest in-scope
    // * client (and its options) are in-scope from operation signature
    public void render(TypeScriptWriter writer) {
    }
}
