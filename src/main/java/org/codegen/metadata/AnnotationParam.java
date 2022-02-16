package org.codegen.metadata;

import org.codegen.metadata.constants.VariableType;

public class AnnotationParam {
    private String name;
    private VariableType type;

    public AnnotationParam(String name, VariableType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }
}
