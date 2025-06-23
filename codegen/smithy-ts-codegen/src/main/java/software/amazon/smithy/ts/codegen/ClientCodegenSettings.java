package software.amazon.smithy.ts.codegen;

import software.amazon.smithy.model.node.ObjectNode;
import software.amazon.smithy.model.shapes.ShapeId;

public class ClientCodegenSettings {
    private final ShapeId service;

    public ClientCodegenSettings(ObjectNode settings) {
        this.service = ShapeId.from(settings.expectStringMember("service").getValue());
    }

    public ShapeId getService() {
        return service;
    }
}
