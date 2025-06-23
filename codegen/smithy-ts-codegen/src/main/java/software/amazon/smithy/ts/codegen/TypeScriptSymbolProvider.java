package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.codegen.core.CodegenException;
import software.amazon.smithy.codegen.core.Symbol;
import software.amazon.smithy.codegen.core.SymbolProvider;
import software.amazon.smithy.model.shapes.Shape;

/**
 * 'Symbols' are the Smithy abstraction for mapping modeled shape types to language-specific types.
 * Code generators, by convention, implement a SymbolProvider that provides that mapping.
 * For this workshop, we will not be exploring this construct, as we can get away with using the names of shapes exactly
 * as they appear in the model. Creation of a WriterDelegator requires it, so we simply provide a stub that will never
 * actually be used.
 */
public class TypeScriptSymbolProvider implements SymbolProvider {
    @Override
    public Symbol toSymbol(Shape shape) {
        throw new CodegenException("unimplemented");
    }
}
