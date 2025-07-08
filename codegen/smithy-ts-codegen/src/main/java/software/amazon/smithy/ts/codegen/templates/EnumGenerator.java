package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.EnumShape;
import software.amazon.smithy.model.traits.EnumValueTrait;
import software.amazon.smithy.ts.codegen.TypeScriptWriter;

public class EnumGenerator {
    private final Model model;
    private final EnumShape enumShape;

    public EnumGenerator(Model model, EnumShape enumShape) {
        this.model = model;
        this.enumShape = enumShape;
    }

    public void render(TypeScriptWriter writer) {
        var name = enumShape.getId().getName();
        writer.write("export type $L = ", name);
        for (var variant : enumShape.getAllMembers().values()) {
            var variantValue = variant
                    .expectTrait(EnumValueTrait.class)
                    .expectStringValue();
            writer.write("| $S", variantValue);
        }
        writer.write("");
    }
}
