package org.codegen.metadata;

import org.codegen.metadata.constants.AssignmentPosition;
import org.codegen.metadata.constants.LiteralType;
import org.codegen.metadata.constants.OPERATORS;
import org.codegen.metadata.constants.VariableType;

public class LiteralMetadata {
    private LiteralType literalType;
    private String varName;
    private LogicStatementMetadata expression ;
    private VariableType variableType ;
    private OPERATORS operator;
    private String userDefinedType ;
    private AssignmentPosition position;
    private boolean isPathVariable ;

    public LiteralMetadata(LiteralType literalType) {
        this.literalType = literalType;
    }

    public LiteralMetadata(LiteralMetadata literalMetadata){
        this.literalType = literalMetadata.getLiteralType();
        this.varName = literalMetadata.getVarName();
        this.variableType = literalMetadata.getVariableType();
        this.operator = literalMetadata.getOperator();
        this.userDefinedType = literalMetadata.getUserDefinedType();
    }

    public LiteralType getLiteralType() {
        return literalType;
    }

    public void setLiteralType(LiteralType literalType) {
        this.literalType = literalType;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public LogicStatementMetadata getExpression() {
        return expression;
    }

    public void setExpression(LogicStatementMetadata expression) {
        this.expression = expression;
    }

    public VariableType getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableType variableType) {
        this.variableType = variableType;
    }

    public OPERATORS getOperator() {
        return operator;
    }

    public void setOperator(OPERATORS operator) {
        this.operator = operator;
    }

    public String getUserDefinedType() {
        return userDefinedType;
    }

    public void setUserDefinedType(String userDefinedType) {
        this.userDefinedType = userDefinedType;
    }

    public AssignmentPosition getPosition() {
        return position;
    }

    public void setPosition(AssignmentPosition position) {
        this.position = position;
    }

    public boolean isPathVariable() {
        return isPathVariable;
    }

    public void setPathVariable(boolean pathVariable) {
        isPathVariable = pathVariable;
    }
}
