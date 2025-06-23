package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.codegen.core.Symbol;

/**
 * Basic abstraction for pulling in runtime code into our generator.
 * We reference these symbols with the '$T' formatter in writer.write() calls, and the writer handles generating import
 * statements for us.
 */
public final class RuntimeTypes {
    public static class HTTP {
        private static Symbol buildSymbol(String name) {
            return RuntimeTypes.buildSymbol(name, "http", "runtime/http");
        }

        public static Symbol doRequest = buildSymbol("doRequest");
        public static Symbol HTTPRequest = buildSymbol("HTTPRequest");
        public static Symbol HTTPResponse = buildSymbol("HTTPResponse");
    }

    public static class SIGV4 {
        private static Symbol buildSymbol(String name) {
            return RuntimeTypes.buildSymbol(name, "sigv4", "runtime/sigv4");
        }

        public static Symbol signRequest = buildSymbol("signRequest");
    }

    public static class CREDENTIALS {
        private static Symbol buildSymbol(String name) {
            return RuntimeTypes.buildSymbol(name, "credentials", "runtime/credentials");
        }

        public static Symbol Credentials = buildSymbol("Credentials");
    }

    /**
     * The Symbol type is the Smithy codegen abstraction for language-native types.
     */
    private static Symbol buildSymbol(String name, String namespace, String module) {
        return Symbol.builder()
                .name(name)
                .namespace(namespace, ".")
                .definitionFile(module)
                .build();
    }
}
