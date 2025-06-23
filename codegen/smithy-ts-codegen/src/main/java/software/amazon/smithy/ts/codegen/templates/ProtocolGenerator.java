package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

/**
 * The protocol generator will generate the logic to
 * - serialize the modeled input request into an HTTP request
 * - transform the received HTTP response into the modeled output type.
 * For this exercise we will only support awsJson 1.0 and we will assume the model is declared to use that protocol.
 */
public class ProtocolGenerator {
    private final Model model;
    private final OperationShape operation;

    public ProtocolGenerator(Model model, OperationShape operation) {
        this.model = model;
        this.operation = operation;
    }

    public void renderSerialize(TypeScriptWriter writer) {
    }

    public void renderDeserialize(TypeScriptWriter writer) {
    }
}
