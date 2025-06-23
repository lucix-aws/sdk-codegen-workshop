package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.EnumShape;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

public class EnumGenerator {
    private final Model model;
    private final EnumShape enumShape;

    public EnumGenerator(Model model, EnumShape enumShape) {
        this.model = model;
        this.enumShape = enumShape;
    }

    public void render(TypeScriptWriter writer) {
    }
}
