package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.model.shapes.ServiceShape;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

/**
 * The protocol generator will generate the logic to
 * - serialize the modeled input request into an HTTP request
 * - transform the received HTTP response into the modeled output type.
 * For this exercise we will only support awsJson 1.0 and we will assume the model is declared to use that protocol.
 */
public class ProtocolGenerator {
    private final Model model;
    private final ServiceShape service;
    private final OperationShape operation;

    public ProtocolGenerator(Model model, ServiceShape service, OperationShape operation) {
        this.model = model;
        this.service = service;
        this.operation = operation;
    }

    public String getHttpMethod() {
        return "POST"; // awsJson 1.0 is always POST
    }

    public String getHttpPath() {
        return "/"; // awsJson 1.0 is always to path / - the X-Amz-Target header dictates the operation
    }

    // implicit contracts:
    // * operation codegen has declared a variable "input" of type corresponding to the operation's input type
    // * operation codegen has declared a variable "request" of type HTTPRequest
    //
    // See https://smithy.io/2.0/aws/protocols/aws-json-1_0-protocol.html#protocol-behaviors for details on how to
    // form the request.
    //
    // HINT: You can ignore serialization of unions, since SQS does not use any. The solution is four lines:
    // * one line to serialize the request body
    // * three lines to set headers required by the protocol
    public void renderSerialize(TypeScriptWriter writer) {
    }

    // implicit contracts:
    // * operation codegen has declared a variable "response" of type HTTPResponse in-scope
    // * this method will declare a variable "output" of type corresponding to the operation's output type
    public void renderDeserialize(TypeScriptWriter writer) {
    }
}
