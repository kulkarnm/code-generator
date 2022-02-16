package org.codegen.metadata;

public class ArgumentMetadata {
    private String argumentName;
    private ClassMetadata argumentType;

    public ArgumentMetadata(String argumentName, ClassMetadata argumentType) {
        this.argumentName = argumentName;
        this.argumentType = argumentType;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public ClassMetadata getArgumentType() {
        return argumentType;
    }
}
