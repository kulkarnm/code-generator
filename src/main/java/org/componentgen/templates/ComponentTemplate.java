package org.componentgen.templates;

import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.LogicStatementMetadata;
import org.codegen.metadata.PackageMetadata;
import org.codegen.metadata.constants.AssignmentPosition;
import org.codegen.metadata.constants.LiteralType;
import org.codegen.metadata.constants.STATEMENTTYPE;

import java.util.List;

public class ComponentTemplate {
    protected PackageMetadata packageMetadata;

    public PackageMetadata getPackageMetadata() {
        return packageMetadata;
    }

    public void setPackageMetadata(PackageMetadata packageMetadata) {
        this.packageMetadata = packageMetadata;
    }

    public LogicStatementMetadata buildMethodCallStatement(int sequence, String callerName, String methodName, LiteralMetadata targetVariable, List<LiteralMetadata> arguments) {
        LogicStatementMetadata statement = new LogicStatementMetadata(sequence);
        statement.setStatementType(STATEMENTTYPE.METHOD_CALL);
        if( null != targetVariable){
            if( targetVariable.getPosition() != AssignmentPosition.TARGET){
                targetVariable.setPosition(AssignmentPosition.TARGET);
            }
            statement.addToLiteralMetadata(targetVariable);
        }
        LiteralMetadata literal2 = new LiteralMetadata(LiteralType.CALLER_NAME);
        literal2.setVarName(callerName);
        LiteralMetadata literal3 = new LiteralMetadata(LiteralType.METHOD_NAME);
        literal3.setVarName(methodName);
        statement.addToLiteralMetadata(literal2);
        statement.addToLiteralMetadata(literal3);
        if( null != arguments && ! arguments.isEmpty()){
            for(LiteralMetadata literal : arguments){
                if(literal.getLiteralType() != LiteralType.ARGUMENT_NAME){
                    statement.reuseLiteralMetadata(literal,LiteralType.ARGUMENT_NAME);
                }else {
                    statement.addToLiteralMetadata(literal);
                }
            }
        }
        return statement;
    }

    public LogicStatementMetadata buildConstructorCallStatement(int sequence,String classOtTypeName, LiteralMetadata targetVariable,List<LiteralMetadata> arguments){
        LogicStatementMetadata statement = new LogicStatementMetadata(sequence);
        statement.setStatementType(STATEMENTTYPE.CONSTRUCTOR_CALL);
        if( null != targetVariable){
            if( targetVariable.getPosition() != AssignmentPosition.TARGET){
                targetVariable.setPosition(AssignmentPosition.TARGET);
            }
            statement.addToLiteralMetadata(targetVariable);
        }
        LiteralMetadata literal3 = new LiteralMetadata(LiteralType.CONSTRUCTOR_TYPE_NAME);
        literal3.setUserDefinedType(classOtTypeName);
        statement.addToLiteralMetadata(literal3);
        if( null != arguments && ! arguments.isEmpty()){
            for(LiteralMetadata literal : arguments){
                if(literal.getLiteralType() != LiteralType.ARGUMENT_NAME){
                    statement.reuseLiteralMetadata(literal,LiteralType.ARGUMENT_NAME);
                }else {
                    statement.addToLiteralMetadata(literal);
                }
            }
        }
        return statement;
    }

    public LogicStatementMetadata buildReturnStatement(int sequence, LiteralMetadata targetVariable){
        LogicStatementMetadata statement = new LogicStatementMetadata(sequence);
        statement.setStatementType(STATEMENTTYPE.RETURN_STATEMENT);
        statement.addToLiteralMetadata(targetVariable);
        return statement;    }
}
