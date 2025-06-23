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
    // To simplify things, our import container just tracks module names and imports using the wildcard ('*').
    private final Set<String> imports = new HashSet<>();

    /**
     * Records the use of a runtime symbol in a code writer. The writer remembers all the imports used, and generates
     * the appropriate TypeScript import statements in the final output.
     */
    @Override
    public void importSymbol(Symbol symbol, String s /* optional alias, unused */) {
        // All we need is the namespace, since we're using wildcard imports.
        // Since we track via a Set, we get deduplication of imports from the same module for free.
        imports.add(symbol.getNamespace());
    }

    public Set<String> getImports() {
        return imports;
    }
}
