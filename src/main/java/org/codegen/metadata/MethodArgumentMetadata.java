package org.codegen.metadata;

public class MethodArgumentMetadata {
    private String argName;
    private String argType;
    private boolean isPathVariable;

    public MethodArgumentMetadata(String argName, String argType, boolean isPathVariable) {
        this.argName = argName;
        this.argType = argType;
        this.isPathVariable = isPathVariable;
    }

    public String getArgName() {
        return argName;
    }

    public String getArgType() {
        return argType;
    }

    public boolean isPathVariable() {
        return isPathVariable;
    }
}
