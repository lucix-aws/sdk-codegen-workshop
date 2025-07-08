package software.amazon.smithy.ts.codegen.templates;

import software.amazon.smithy.codegen.core.CodegenException;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.CollectionShape;
import software.amazon.smithy.model.shapes.ListShape;
import software.amazon.smithy.model.shapes.MapShape;
import software.amazon.smithy.model.shapes.MemberShape;
import software.amazon.smithy.model.shapes.Shape;
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
        writer.openBlock("export type $L = {", "}", structure.getId().getName(), () -> {
            for (var member : structure.getAllMembers().entrySet()) {
                var memberName = member.getKey();
                var memberShape = model.expectShape(member.getValue().getTarget());

                writer.write("$L?: $L", memberName, toSymbol(memberShape));
            }
        });
        writer.write("");
    }

    private String toSymbol(Shape shape) {
        return switch (shape.getType()) {
            case BOOLEAN -> "boolean";
            case STRING -> "string";
            case TIMESTAMP -> "Date";
            case BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE -> "number";
            case LIST -> {
                var listTarget = ((ListShape) shape).getMember().getTarget();
                yield toSymbol(model.expectShape(listTarget)) + "[]";
            }
            case MAP -> {
                var mapTarget = ((MapShape) shape).getValue().getTarget();
                yield "Record<string, " + toSymbol(model.expectShape(mapTarget)) + ">";
            }
            case STRUCTURE, ENUM -> shape.getId().getName();
            case BLOB -> "Blob";
            default -> throw new CodegenException("Unsupported type: " + shape.getType());
        };
    }
}
