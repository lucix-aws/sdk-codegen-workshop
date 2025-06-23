package software.amazon.smithy.ts.codegen;

import java.util.HashSet;
import java.util.Set;
import software.amazon.smithy.model.Model;
import software.amazon.smithy.model.shapes.Shape;

public class ShapeUtil {
    /**
     * Returns to us all shapes referenced by a root shape, recursively.
     */
    public static Set<Shape> getReferences(Model model, Shape root) {
        Set<Shape> refs = new HashSet<>();
        refs.add(root);
        for (var member : root.members()) {
            refs.addAll(getReferences(model, model.expectShape(member.getTarget())));
        }
        return refs;
    }
}
