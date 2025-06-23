package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.model.shapes.ServiceShape;
import software.amazon.smithy.model.shapes.StructureShape;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

/**
 * This will generate the logic for each operation.
 */
public class OperationGenerator {
    private final Model model;
    private final ServiceShape service;
    private final OperationShape operation;

    public OperationGenerator(Model model, ServiceShape service, OperationShape operation) {
        this.model = model;
        this.service = service;
        this.operation = operation;
    }

    /**
     * At a high level, render() must generate a function with code that performs the following:
     * 1. Serialize the input structure into an HTTP request, in the format dictated by the protocol being used
     *    (awsJson 1.0 for us).
     * 2. Determine the endpoint (AKA hostname) where the request will be sent. This is known as "endpoint resolution".
     *    The resolved endpoint will vary by the region in our config.
     */
    public void render(TypeScriptWriter writer) {
        var protocolGenerator = new ProtocolGenerator(model, service, operation);

        StructureShape input = model.expectShape(operation.getInputShape(), StructureShape.class);
        StructureShape output = model.expectShape(operation.getOutputShape(), StructureShape.class);
        String opName = operation.getId().getName();
        String inputName = input.getId().getName();
        String outputName = output.getId().getName();

      //writer.openBlock("const do$L = (client: $L, input: $L): Promise<$L> => {", "}",
      //        opName, ClientGenerator.STRUCTURE_NAME, inputName, outputName,
      //        () -> {
      //            // ...
      //        });
    }
}
