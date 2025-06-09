package software.amazon.smithy.lua.codegen;

import software.amazon.smithy.build.PluginContext;
import software.amazon.smithy.build.SmithyBuildPlugin;
import software.amazon.smithy.utils.SmithyInternalApi;

@SmithyInternalApi
public final class TypeScriptClientCodegenPlugin implements SmithyBuildPlugin {
    @Override
    public String getName() {
        return "ts-client-codegen";
    }

    @Override
    public void execute(PluginContext context) {
    }
}
