package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.aws.traits.ServiceTrait;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.model.shapes.ServiceShape;
import software.amazon.smithy.model.shapes.StructureShape;
import software.amazon.smithy.ts.codegen.RuntimeTypes;
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

        writer.openBlock("const do$L = async (client: $L, input: $L): Promise<$L> => {", "}",
                opName, ClientGenerator.STRUCTURE_NAME, inputName, outputName,
                () -> {
                    // "resolve" our endpoint. we will assume standard AWS regional endpoint scheme.
                    // normally this happens dynamically after serialization, but in this simplified workshop we can
                    // just do it now
                    var endpointPrefix = service.expectTrait(ServiceTrait.class).getEndpointPrefix();
                    writer.write("const endpoint = `$L.$${client.options.region}.amazonaws.com`", endpointPrefix);

                    // initialize our http request
                    writer.openBlock("const request: $T = {", "}", RuntimeTypes.HTTP.HTTPRequest, () -> {
                        writer.write("method: $S,", protocolGenerator.getHttpMethod());
                        writer.write("path: $S,", protocolGenerator.getHttpPath());
                        writer.write("host: endpoint,");
                        writer.write("headers: {},");
                    });

                    // serialize the input
                    protocolGenerator.renderSerialize(writer);
                    writer.write("");

                    // generate auth code to sign the request with sigv4
                    new AuthGenerator(model, service).render(writer);
                    writer.write("");

                    // do the HTTP request
                    writer.write("const response = await $T(request)", RuntimeTypes.HTTP.doRequest);
                    writer.openBlock("if (response.statusCode !== 200) {", "}", () -> {
                        writer.write("throw new Error('operation failed!')");
                    });
                    writer.write("");

                    // deserialize and return the output
                    protocolGenerator.renderDeserialize(writer);
                    writer.write("return output");
                });
        writer.write("");
    }
}
