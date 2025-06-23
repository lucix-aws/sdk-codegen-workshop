package software.amazon.smithy.ts.codegen;

import java.util.Set;
import software.amazon.smithy.build.PluginContext;
import software.amazon.smithy.build.SmithyBuildPlugin;
import software.amazon.smithy.model.knowledge.TopDownIndex;
import software.amazon.smithy.model.shapes.EnumShape;
import software.amazon.smithy.model.shapes.OperationShape;
import software.amazon.smithy.model.shapes.ServiceShape;
import software.amazon.smithy.model.shapes.StructureShape;
import software.amazon.smithy.ts.codegen.templates.ClientGenerator;
import software.amazon.smithy.ts.codegen.templates.ClientOptionsGenerator;
import software.amazon.smithy.ts.codegen.templates.EnumGenerator;
import software.amazon.smithy.ts.codegen.templates.OperationGenerator;
import software.amazon.smithy.ts.codegen.templates.StructureGenerator;

/**
 * This is our entry point into code generation.
 */
public final class TypeScriptClientCodegenPlugin implements SmithyBuildPlugin {
    @Override
    public String getName() {
        return "ts-client-codegen";
    }

    @Override
    public void execute(PluginContext context) {
        // create our strongly-typed settings container from the smithy-build.json settings
        var settings = new ClientCodegenSettings(context.getSettings());

        // create our writer delegator
        var writerDelegator = new TypeScriptWriterDelegator(context.getFileManifest(), new TypeScriptSymbolProvider());

        // test our settings
        System.out.println("----------------------------");
        System.out.println("hello from our build plugin!");
        System.out.println("you have selected the service: " + settings.getService());
        System.out.println("----------------------------");

        // The plugin context gives us the model from which we will generate a client.
        var model = context.getModel();
        var service = model.expectShape(settings.getService(), ServiceShape.class);

        // In a production SDK, you would split up your generated output across multiple files - but for simplicity here
        // we will use a single file.
        writerDelegator.useFileWriter("client.ts", (writer) -> {
            // 1. generate the service client and options structures
            new ClientOptionsGenerator().render(writer);
            new ClientGenerator(model, service).render(writer);

            // 2. generate types for all structures and enums in the model
            //    (this will include input and output structures)
            Set<StructureShape> structures = model.getStructureShapes();
            Set<EnumShape> enumShapes = model.getEnumShapes();
            for (var structure : structures) {
                new StructureGenerator(model, structure).render(writer);
            }
            for (var enumShape : enumShapes) {
                new EnumGenerator(model, enumShape).render(writer);
            }

            // 3. generate operation implementations
            //    the operation generator will delegate to the ProtocolGenerator to generate serde code
            Set<OperationShape> operations = TopDownIndex.of(model).getContainedOperations(service);
            for (var operation : operations) {
                new OperationGenerator(model, service, operation).render(writer);
            }
        });

        // write files to disk
        writerDelegator.flushWriters();
    }
}
