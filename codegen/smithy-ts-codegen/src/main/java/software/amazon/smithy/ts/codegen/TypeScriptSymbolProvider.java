package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.codegen.core.CodegenException;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.model.shapes.Shape;

/**
 * 'Symbols' are the Smithy abstraction for mapping modeled shape types to language-specific types.
 * Code generators, by convention, implement a SymbolProvider that provides that mapping.
 */
public class TypeScriptSymbolProvider implements SymbolProvider {
    @Override
    public Symbol toSymbol(Shape shape) {
        throw new CodegenException("TODO");
    }
}
