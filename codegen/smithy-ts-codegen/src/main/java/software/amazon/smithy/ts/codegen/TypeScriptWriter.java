package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.codegen.core.SymbolWriter;

/**
 * TypeScriptWriter is the primary abstraction we use to generate our source code.
 */
public class TypeScriptWriter extends SymbolWriter<TypeScriptWriter, TypeScriptImportContainer> {
    public TypeScriptWriter() {
        super(new TypeScriptImportContainer());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
