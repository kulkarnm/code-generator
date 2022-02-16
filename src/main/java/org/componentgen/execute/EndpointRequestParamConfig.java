package org.componentgen.execute;

public class EndpointRequestParamConfig {
    private String argumentName;
    private String argumentType;
    private boolean isPathVariable;

    public EndpointRequestParamConfig(String argumentName, String argumentType, boolean isPathVariable) {
        this.argumentName = argumentName;
        this.argumentType = argumentType;
        this.isPathVariable = isPathVariable;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }

    public String getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(String argumentType) {
        this.argumentType = argumentType;
    }

    public boolean isPathVariable() {
        return isPathVariable;
    }

    public void setPathVariable(boolean pathVariable) {
        isPathVariable = pathVariable;
    }
}
