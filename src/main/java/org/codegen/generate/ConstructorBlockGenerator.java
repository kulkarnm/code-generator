package org.codegen.generate;

import com.helger.jcodemodel.*;
import org.codegen.metadata.ConstructorMetadata;
import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.LogicStatementMetadata;
import org.codegen.metadata.constants.AssignmentPosition;
import org.codegen.metadata.constants.CLASSTYPE;
import org.codegen.metadata.constants.LiteralType;
import org.codegen.metadata.constants.STATEMENTTYPE;

import java.util.ArrayList;
import java.util.List;

public class ConstructorBlockGenerator extends SegmentGenerator<ConstructorMetadata> {
    private JMethod jMethod;
    public ConstructorBlockGenerator(JMethod jMethod, JCodeModel cm){
        super(cm);
        this.jMethod = jMethod;
    }

    public void generateSegment(ConstructorMetadata constructorMetadata) throws JClassAlreadyExistsException {
        List<LogicStatementMetadata> blockStatements = constructorMetadata.getBlockStatements();
        if(constructorMetadata.getParentClassMetadata().getClasstype() != CLASSTYPE.INTERFACE
        || constructorMetadata.getParentClassMetadata().getClasstype() != CLASSTYPE.ENUM){
            generateBlockSegments(blockStatements,jMethod.body());
        }
    }

    private JBlock generateBlockSegments(List<LogicStatementMetadata> blockStatements, JBlock jMethodBody) throws JClassAlreadyExistsException {
        for(LogicStatementMetadata blockStatement : blockStatements){
            if(blockStatement.getStatementType() == STATEMENTTYPE.LOG){

            }else if(blockStatement.getStatementType() == STATEMENTTYPE.ASSIGNMENT) {
                buildAssignmentStatement(blockStatement,jMethodBody);
            }else if(blockStatement.getStatementType() == STATEMENTTYPE.EXCEPTION_BLOCK){

            }else if(blockStatement.getStatementType() == STATEMENTTYPE.SUPER_CALL){
                buildSuperCallStatement(blockStatement,jMethodBody);
            }else if (blockStatement.getStatementType() == STATEMENTTYPE.CONSTRUCTOR_CALL ){
                buildConstructorCallStatement(blockStatement,jMethodBody);
            }
        }
        return jMethodBody;
    }

    private JBlock buildAssignmentStatement(LogicStatementMetadata logicStatementMetadata, JBlock jMethodBody) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        String sourceVarName = null;
        String destinationVarName = null;
        for(int i = 0; i < operandsAndOperators.size();i++){
            LiteralMetadata literal = operandsAndOperators.get(i);
            if(literal.getLiteralType() == LiteralType.VAR_NAME || literal.getLiteralType() == LiteralType.VAR_REF) {
                if(literal.getPosition() == AssignmentPosition.SOURCE){
                    sourceVarName = literal.getVarName();
                }else if(literal.getPosition() == AssignmentPosition.TARGET) {
                    destinationVarName = literal.getVarName();
                }
            }
        }
        jMethodBody.assign(JExpr._this().ref(destinationVarName), JExpr.ref(sourceVarName));
        return jMethodBody;
    }

    private JBlock buildConstructorCallStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        boolean defineAndInitialise = false;
        if(logicStatementMetadata.getStatementType()==STATEMENTTYPE.CONSTRUCTOR_CALL){
            String constructorType = null;
            List<LiteralMetadata> arguments = new ArrayList<>();
            List<IJExpression> expressionsAsArguments = new ArrayList<>();
            String returnVarName = null;
            boolean isInstanceVariable = false;
            for(LiteralMetadata literal : operandsAndOperators){
                if(literal.getLiteralType() == LiteralType.CONSTRUCTOR_TYPE_NAME){
                    constructorType = literal.getUserDefinedType();
                }else if(literal.getLiteralType() == LiteralType.ARGUMENT_NAME){
                    arguments.add(literal);
                }else if(literal.getLiteralType() ==LiteralType.VAR_NAME && literal.getPosition() == AssignmentPosition.TARGET){
                    returnVarName = literal.getVarName();
                    defineAndInitialise=true;
                }else if(literal.getLiteralType() == LiteralType.VAR_REF && literal.getPosition() == AssignmentPosition.TARGET){
                    returnVarName = literal.getVarName();
                    defineAndInitialise=false;
                    isInstanceVariable=true;
                }else if(literal.getLiteralType() == LiteralType.ARGUMENT_AS_EXPRESSION){
                    JBlock innerBlock = new JBlock();
                    LogicStatementMetadata expression = literal.getExpression() ;
                    List<LogicStatementMetadata> expressions = new ArrayList<>();
                    expressions.add(expression);
                    innerBlock = generateBlockSegments(expressions,innerBlock);
                    expressionsAsArguments.add((IJExpression) innerBlock.getContents().get(0));
                }
            }

            if(constructorType != null){
                AbstractJClass constructorClass = getCm().ref(constructorType);
                if(null == constructorClass){
                    constructorClass = getCm()._class(constructorType);
                }
                JInvocation rhsAssignmentExpression = JExpr._new(constructorClass);
                if(null != arguments && !arguments.isEmpty()){
                    for(int i =0; i< arguments.size();i++){
                        rhsAssignmentExpression.arg(JExpr.ref(arguments.get(i).getVarName()));
                    }
                }
                if(null != expressionsAsArguments && !expressionsAsArguments.isEmpty()){
                    for(int j=0;j < expressionsAsArguments.size();j++){
                        rhsAssignmentExpression.arg(expressionsAsArguments.get(j));
                    }
                }

                if(constructorClass != null ){
                    if(null != returnVarName){
                        if(defineAndInitialise){
                            jMethodBody.decl(constructorClass,returnVarName,rhsAssignmentExpression);
                        }else if(isInstanceVariable){
                            jMethodBody.assign(JExpr._this().ref(returnVarName), rhsAssignmentExpression);
                        }else {
                            jMethodBody.assign(JExpr.ref(returnVarName), rhsAssignmentExpression);
                        }
                    }else {
                        jMethodBody.add(rhsAssignmentExpression);
                    }
                }
            }
        }
        return jMethodBody;
    }

    private JBlock buildSuperCallStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody) {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        if(logicStatementMetadata.getStatementType() == STATEMENTTYPE.SUPER_CALL){
            List<LiteralMetadata> arguments = new ArrayList<>();
            for(LiteralMetadata literal : operandsAndOperators){
                if(literal.getLiteralType() == LiteralType.ARGUMENT_NAME){
                    arguments.add(literal);
                }

            }
            JInvocation invocation = jMethodBody.invoke("super");
            if(null != arguments && !arguments.isEmpty()){
                for(LiteralMetadata literal : arguments){
                    invocation.arg(JExpr.ref(literal.getVarName()));
                }
            }
        }
        return jMethodBody;
    }
}
