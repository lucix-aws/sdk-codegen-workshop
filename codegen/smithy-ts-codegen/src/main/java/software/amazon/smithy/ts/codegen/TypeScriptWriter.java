package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.codegen.core.CodegenException;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolWriter;

/**
 * TypeScriptWriter is the primary abstraction we use to generate our source code.
 */
public class TypeScriptWriter extends SymbolWriter<TypeScriptWriter, TypeScriptImportContainer> {
    public TypeScriptWriter() {
        super(new TypeScriptImportContainer());

        // Smithy's abstract SymbolWriter allows us to register custom format markers to be used with additional
        // arguments passed to write() calls.
        //
        // We define a single custom formatter, used in write() as $T, which allows us to automatically inject import
        // statements and handle namespace prefixes for types (Symbols) where required.
        //
        // e.g. writer.write("const credentials: $T = { /* ... */ };", RuntimeTypes.CREDENTIALS.Credentials);
        putFormatter('T', (arg, indent /* unused */) -> {
            if (!(arg instanceof Symbol symbol)) {
                throw new CodegenException("Expected a symbol but got " + arg);
            }

            // record our symbol so we can generate the appropriate import statement later
            getImportContainer().importSymbol(symbol);

            // import as <namespace>.<name> e.g. sigv4.signRequest
            return symbol.getNamespace() + "." + symbol.getName();
        });
    }

    /**
     * Prepends runtime import statements to our generated code.
     */
    @Override
    public String toString() {
        var imports = new StringBuilder();
        for (var moduleName : getImportContainer().getImports()) {
            // In our workshop we are assuming that the entire runtime is placed in the same directory as the generated
            // code. Thus, we simply render our import statements using relative paths.
            //
            // In a production SDK, the runtime would generally be published as a separate module/package/etc.
            imports.append(String.format("import * as %s from './runtime/%s.ts'\n", moduleName, moduleName));
        }

        // super.toString() here will give us everything we previously generated with write() calls.
        return imports + "\n" + super.toString();
    }
}
