package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.build.PluginContext;
import software.amazon.smithy.build.SmithyBuildPlugin;
import software.amazon.smithy.codegen.core.WriterDelegator;
import software.amazon.smithy.utils.SmithyInternalApi;

/**
 * This is our entry point into code generation.
 */
@SmithyInternalApi
public final class TypeScriptClientCodegenPlugin implements SmithyBuildPlugin {
    @Override
    public String getName() {
        return "ts-client-codegen";
    }

    @Override
    public void execute(PluginContext context) {
        // create our strongly-typed settings container from the smithy-build.json settings
        ClientCodegenSettings settings = new ClientCodegenSettings(context.getSettings());

        // create our symbol provider and writer delegator
        var symbolProvider = new TypeScriptSymbolProvider();
        TypeScriptWriterDelegator writerDelegator = new TypeScriptWriterDelegator(context.getFileManifest(), symbolProvider);

        // test our settings
        System.out.println("----------------------------");
        System.out.println("hello from our build plugin!");
        System.out.println("you have selected the service: " + settings.getService());
        System.out.println("----------------------------");

        // write files to disk
        writerDelegator.flushWriters();
    }
}
