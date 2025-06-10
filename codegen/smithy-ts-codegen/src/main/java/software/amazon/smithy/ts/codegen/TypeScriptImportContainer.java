package software.amazon.smithy.ts.codegen;

import java.util.HashSet;
import java.util.Set;
import software.amazon.smithy.codegen.core.ImportContainer;
import software.amazon.smithy.codegen.core.Symbol;

/**
 * TypeScriptImportContainer is a simple wrapper for tracking external symbols referenced in a code writer.
 * Every TypeScriptWriter has its own internal import container which is used to generate TypeScript 'import' statements
 * in the output.
 */
public class TypeScriptImportContainer implements ImportContainer {
    private final Set<Symbol> imports = new HashSet<>();

    @Override
    public void importSymbol(Symbol symbol, String s) {
        imports.add(symbol);
    }

    public Set<Symbol> getImports() {
        return imports;
    }
}
