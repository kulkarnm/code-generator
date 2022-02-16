package org.componentgen.config;

public class PathVariableConfig {
    private String pathVariableName ;
    private String pathVariableValue ;

    public PathVariableConfig(String pathVariableName, String pathVariableValue ){
        this.pathVariableName=pathVariableName;
        this.pathVariableValue = pathVariableValue;
    }

    public PathVariableConfig(){}

    public String getPathVariableName() {
        return pathVariableName;
    }

    public void setPathVariableName(String pathVariableName) {
        this.pathVariableName = pathVariableName;
    }

    public String getPathVariableValue() {
        return pathVariableValue;
    }

    public void setPathVariableValue(String pathVariableValue) {
        this.pathVariableValue = pathVariableValue;
    }

    public void set(String key,String value){
        if(key.equals("pathVariableName")){
            this.setPathVariableName(value);
        }else if(key.equals("pathVariableValue")){
            this.setPathVariableValue(value);
        }
    }
}
