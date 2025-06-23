package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.StructureShape;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

/**
 * Generates types for structure shapes (shapes with "type": "structure"). The code generator is responsible for mapping
 * a modeled structure to its language-native form.
 */
public class StructureGenerator {
    private final Model model;
    private final StructureShape structure;

    public StructureGenerator(Model model, StructureShape structure) {
        this.model = model;
        this.structure = structure;
    }

    public void render(TypeScriptWriter writer) {
    }
}
