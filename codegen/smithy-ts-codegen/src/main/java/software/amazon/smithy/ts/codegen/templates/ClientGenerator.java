package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.ServiceShape;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

/**
 * This will generate the client structure that SDK users create and use to call service operations.
 */
public class ClientGenerator {
    public static final String STRUCTURE_NAME = "Client";

    private final Model model;
    private final ServiceShape service;

    public ClientGenerator(Model model, ServiceShape service) {
        this.model = model;
        this.service = service;
    }

    /**
     * render() will generate a class declaration for our service client with the following:
     * - Its constructor, which accepts a single argument that is our "options" structure.
     * - Method declarations for each service operation. Note that we will NOT generate the full logic for each
     *   operation here. Instead, we will generate the implementations separately in our OperationGenerator class as
     *   individual functions. Our method declarations within the service client class will then call those operations.
     *   e.g.
     *   ```
     *   public ListQueues(input: ListQueuesInput): ListQueuesOutput {
     *     return listQueuesOperation(this.options, input); // listQueuesOperation is created by OperationGenerator
     *   }
     *   ```
     */
    public void render(TypeScriptWriter writer) {
        writer.openBlock("export class $L {", "}", STRUCTURE_NAME, () -> {
            // 1. render constructor
            // ...

            // 2. render operation APIs
            // ...
        });
    }
}
