package org.codegen.generate;

import com.helger.jcodemodel.*;
import org.codegen.metadata.ConstructorMetadata;
import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.LogicStatementMetadata;
import org.codegen.metadata.MethodMetadata;
import org.codegen.metadata.constants.*;

import java.math.BigDecimal;
import java.util.*;

public class MethodBlockGenerator extends SegmentGenerator<MethodMetadata>{

    private JMethod jMethod;

    public MethodBlockGenerator(JMethod jMethod, JCodeModel cm) {
        super(cm);
        this.jMethod = jMethod;
    }

    public void generateSegment(MethodMetadata methodMetadata) throws JClassAlreadyExistsException {
        List<LogicStatementMetadata> blockStatements = methodMetadata.getBlockStatements();
        if (methodMetadata.getParentClassMetadata().getClasstype() != CLASSTYPE.INTERFACE
                || methodMetadata.getParentClassMetadata().getClasstype() != CLASSTYPE.ENUM) {
            generateBlockSegments(blockStatements, jMethod.body());
        }
    }

    private JBlock generateBlockSegments(List<LogicStatementMetadata> blockStatements, JBlock jMethodBody) throws JClassAlreadyExistsException {
        for (LogicStatementMetadata blockStatement : blockStatements) {
            if (blockStatement.getStatementType() == STATEMENTTYPE.LOG) {
                buildLogStatement(blockStatement, jMethodBody);
            } else if (blockStatement.getStatementType() == STATEMENTTYPE.ASSIGNMENT) {
                buildAssignmentStatement(blockStatement, jMethodBody);
            } else if (blockStatement.getStatementType() == STATEMENTTYPE.EXCEPTION_BLOCK) {
                buildExceptionStatement(blockStatement, jMethodBody);
            }else if(blockStatement.getStatementType() == STATEMENTTYPE.IF_CONDITION){
                buildIfConditionStatement(blockStatement,jMethodBody);
            }else if(blockStatement.getStatementType() == STATEMENTTYPE.IF_ELSE_CONDITION){
                buildIfConditionStatement(blockStatement,jMethodBody);
            }else if(blockStatement.getStatementType() == STATEMENTTYPE.IF_ELSEIF_ELSE_CONDITION){
                buildIfConditionStatement(blockStatement,jMethodBody);
            }else if(blockStatement.getStatementType() == STATEMENTTYPE.LOOP){

            }else if(blockStatement.getStatementType() == STATEMENTTYPE.METHOD_CALL){
                buildMethodCall(blockStatement,jMethodBody,null);
            }else if (blockStatement.getStatementType() == STATEMENTTYPE.CONSTRUCTOR_CALL) {
                buildConstructorCallStatement(blockStatement, jMethodBody);
            }else if (blockStatement.getStatementType() == STATEMENTTYPE.VARIABLE_DECLARATION) {
                buildVariableDeclaration(blockStatement, jMethodBody);
            }else if (blockStatement.getStatementType() == STATEMENTTYPE.RETURN_STATEMENT) {
                buildReturnStatement(blockStatement, jMethodBody);
            }else if (blockStatement.getStatementType() == STATEMENTTYPE.SETTER_STATEMENT) {
                buildSetterStatement(blockStatement, jMethodBody);
            }else if (blockStatement.getStatementType() == STATEMENTTYPE.SUPER_CALL) {
                buildSuperCallStatement(blockStatement, jMethodBody);
            }
        }
        return jMethodBody;
    }

    private JBlock buildSuperCallStatement(LogicStatementMetadata logicStatementMetadata, JBlock jMethodBody) {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        if (logicStatementMetadata.getStatementType() == STATEMENTTYPE.SUPER_CALL) {
            List<LiteralMetadata> arguments = new ArrayList<>();
            for (LiteralMetadata literal : operandsAndOperators) {
                if (literal.getLiteralType() == LiteralType.ARGUMENT_NAME) {
                    arguments.add(literal);
                }

            }
            JInvocation invocation = jMethodBody.invoke("super");
            if (null != arguments && !arguments.isEmpty()) {
                for (LiteralMetadata literal : arguments) {
                    invocation.arg(JExpr.ref(literal.getVarName()));
                }
            }
        }
        return jMethodBody;
    }

    private JBlock buildAssignmentStatement(LogicStatementMetadata logicStatementMetadata, JBlock jMethodBody) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        List<LogicStatementMetadata> innerBlockStatements = logicStatementMetadata.getInnerBlockStatements();
        AbstractJClass destinationClass = null;
        String destinationVarName = null;
        IJExpression rhsAssignmentExpression = null;
        boolean defineAndInitialize=false;

        for (int i = 0; i < operandsAndOperators.size(); i++) {
            LiteralMetadata literal = operandsAndOperators.get(i);
            if (literal.getLiteralType() == LiteralType.VAR_NAME || literal.getLiteralType() == LiteralType.VAR_REF) {
                if (literal.getPosition() == AssignmentPosition.SOURCE) {
                    if(literal.getOperator() == OPERATORS.ASSIGNMENT){
                        rhsAssignmentExpression = JExpr.ref(literal.getVarName());
                    }else if(literal.getOperator() == OPERATORS.CONSTRUCT_AND_ASSIGN){
                        AbstractJClass variableClass = getCm().ref(literal.getUserDefinedType());
                        if(null == variableClass){
                            variableClass=getCm()._class(literal.getUserDefinedType());
                        }
                        rhsAssignmentExpression= JExpr._new(variableClass);
                    }
                } else if (literal.getPosition() == AssignmentPosition.TARGET) {
                    destinationVarName = literal.getVarName();
                    if(literal.getLiteralType()==LiteralType.VAR_NAME){
                        destinationClass = getCm().ref(literal.getUserDefinedType());
                        if( null == destinationClass){
                            destinationClass = getCm()._class(literal.getUserDefinedType());
                        }
                        defineAndInitialize=true;
                    }else {

                    }
                }
            }
        }

        if( null != innerBlockStatements && !innerBlockStatements.isEmpty()){
            JBlock innerBlock = new JBlock();
            jMethodBody.add(generateBlockSegments(innerBlockStatements,innerBlock));
        }
        if(defineAndInitialize){
            jMethodBody.decl(destinationClass,destinationVarName,rhsAssignmentExpression);
        }else {
            jMethodBody.assign(JExpr.ref(destinationVarName), rhsAssignmentExpression);
        }
        return jMethodBody;
    }

    private JVar resolveLiteralToJVar(LiteralMetadata literal,JBlock jMethodBody) throws JClassAlreadyExistsException{
        if(literal.getVariableType() == VariableType.USER_DEFINED) {
            AbstractJClass variableClass = getCm().ref(literal.getUserDefinedType());
            if (null == variableClass) {
                variableClass = getCm()._class(literal.getUserDefinedType());
            }
            return jMethodBody.decl(variableClass,literal.getVarName());
        }else if(literal.getVariableType() == VariableType.INT){
            return jMethodBody.decl(getCm().ref(Integer.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.LONG){
            return jMethodBody.decl(getCm().ref(Long.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.DOUBLE){
            return jMethodBody.decl(getCm().ref(Double.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.FLOAT){
            return jMethodBody.decl(getCm().ref(Float.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.CHAR){
            return jMethodBody.decl(getCm().ref(Character.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.BYTE){
            return jMethodBody.decl(getCm().ref(Byte.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.BOOLEAN){
            return jMethodBody.decl(getCm().ref(Boolean.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.BIGDECIMAL){
            return jMethodBody.decl(getCm().ref(BigDecimal.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.ARRAYLIST){
            return jMethodBody.decl(getCm().ref(ArrayList.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.HASHMAP){
            return jMethodBody.decl(getCm().ref(HashMap.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.HASHSET){
            return jMethodBody.decl(getCm().ref(HashSet.class),literal.getVarName());
        }else if(literal.getVariableType() == VariableType.TREESET){
            return jMethodBody.decl(getCm().ref(TreeSet.class),literal.getVarName());
        }else{
            return null;
        }
    }

    private JBlock buildLogStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody){
        StringBuilder statement = new StringBuilder("LOG.debug(\"");
        for(LiteralMetadata literal : logicStatementMetadata.getOrderedLiteralsUsed()) {
            statement.append(literal.getUserDefinedType());
        }
        statement.append("\")");
        jMethodBody.directStatement(statement.toString());
        return jMethodBody;
    }

    private JBlock buildConstructorCallStatement(LogicStatementMetadata logicStatementMetadata, JBlock jMethodBody) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        boolean defineAndInitialise = false;
        if (logicStatementMetadata.getStatementType() == STATEMENTTYPE.CONSTRUCTOR_CALL) {
            String constructorType = null;
            List<LiteralMetadata> arguments = new ArrayList<>();
            List<IJExpression> expressionsAsArguments = new ArrayList<>();
            String returnVarName = null;
            //boolean isInstanceVariable = false;
            for (LiteralMetadata literal : operandsAndOperators) {
                if (literal.getLiteralType() == LiteralType.CONSTRUCTOR_TYPE_NAME) {
                    constructorType = literal.getUserDefinedType();
                } else if (literal.getLiteralType() == LiteralType.ARGUMENT_NAME) {
                    arguments.add(literal);
                } else if (literal.getLiteralType() == LiteralType.VAR_NAME && literal.getPosition() == AssignmentPosition.TARGET) {
                    returnVarName = literal.getVarName();
                    defineAndInitialise = true;
                } else if (literal.getLiteralType() == LiteralType.VAR_REF && literal.getPosition() == AssignmentPosition.TARGET) {
                    returnVarName = literal.getVarName();
                    defineAndInitialise = false;
                } else if (literal.getLiteralType() == LiteralType.ARGUMENT_AS_EXPRESSION) {
                    JBlock innerBlock = new JBlock();
                    LogicStatementMetadata expression = literal.getExpression();
                    List<LogicStatementMetadata> expressions = new ArrayList<>();
                    expressions.add(expression);
                    innerBlock = generateBlockSegments(expressions, innerBlock);
                    expressionsAsArguments.add((IJExpression) innerBlock.getContents().get(0));
                }
            }

            if (constructorType != null) {
                AbstractJClass constructorClass = getCm().ref(constructorType);
                if (null == constructorClass) {
                    constructorClass = getCm()._class(constructorType);
                }
                JInvocation rhsAssignmentExpression = JExpr._new(constructorClass);
                if (null != arguments && !arguments.isEmpty()) {
                    for (int i = 0; i < arguments.size(); i++) {
                        rhsAssignmentExpression.arg(JExpr.ref(arguments.get(i).getVarName()));
                    }
                }
                if (null != expressionsAsArguments && !expressionsAsArguments.isEmpty()) {
                    for (int j = 0; j < expressionsAsArguments.size(); j++) {
                        rhsAssignmentExpression.arg(expressionsAsArguments.get(j));
                    }
                }

                if (constructorClass != null) {
                    if (null != returnVarName) {
                        if (defineAndInitialise) {
                            jMethodBody.decl(constructorClass, returnVarName, rhsAssignmentExpression);
                        }  else {
                            jMethodBody.assign(JExpr.ref(returnVarName), rhsAssignmentExpression);
                        }
                    } else {
                        jMethodBody.add(rhsAssignmentExpression);
                    }
                }
            }
        }
        return jMethodBody;
    }

    private JBlock buildMethodCall(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody,JInvocation invocation) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        List<LogicStatementMetadata> chainedCallStatements = logicStatementMetadata.getChainedCallStatements();
        boolean defineAndInitialise = false;
        if (logicStatementMetadata.getStatementType() == STATEMENTTYPE.METHOD_CALL) {
            String caller = null;
            String methodName=null;
            List<LiteralMetadata> arguments = new ArrayList<>();
            List<IJExpression> expressionsAsArguments = new ArrayList<>();
            String returnType=null;
            String returnVarName = null;
            for (LiteralMetadata literal : operandsAndOperators) {
                if (literal.getLiteralType() == LiteralType.CALLER_NAME) {
                    caller = literal.getVarName();
                } else if (literal.getLiteralType() == LiteralType.METHOD_NAME) {
                   methodName = literal.getVarName();
                } else if (literal.getLiteralType() == LiteralType.ARGUMENT_NAME) {
                    arguments.add(literal);
                } else if (literal.getLiteralType() == LiteralType.VAR_NAME && literal.getPosition() == AssignmentPosition.TARGET) {
                    returnType=literal.getUserDefinedType();
                    returnVarName = literal.getVarName();
                    defineAndInitialise = true;
                } else if (literal.getLiteralType() == LiteralType.VAR_REF && literal.getPosition() == AssignmentPosition.TARGET) {
                    returnType=literal.getUserDefinedType();
                    returnVarName = literal.getVarName();
                    defineAndInitialise = false;
                } else if (literal.getLiteralType() == LiteralType.ARGUMENT_AS_EXPRESSION) {
                    JBlock innerBlock = new JBlock();
                    LogicStatementMetadata expression = literal.getExpression();
                    List<LogicStatementMetadata> expressions = new ArrayList<>();
                    expressions.add(expression);
                    innerBlock = generateBlockSegments(expressions, innerBlock);
                    expressionsAsArguments.add((IJExpression) innerBlock.getContents().get(0));
                }
            }
            IJExpression callerExpression = null;

            if (null == invocation) {
                callerExpression = JExpr.ref(caller);
                invocation = callerExpression.invoke(methodName);
            }else {
                callerExpression = invocation;
                invocation = callerExpression.invoke(methodName);
            }
            if(null != arguments && !arguments.isEmpty()) {
                for(int i = 0;i<arguments.size();i++){
                    invocation.arg(JExpr.ref(arguments.get(i).getVarName()));
                }
            }

            if(null != expressionsAsArguments && !expressionsAsArguments.isEmpty()) {
                for(int j = 0;j<expressionsAsArguments.size();j++){
                    invocation.arg(expressionsAsArguments.get(j));
                }
            }
            boolean chained =false;
            if(null != chainedCallStatements && !chainedCallStatements.isEmpty()){
                for(LogicStatementMetadata chainedStatement: chainedCallStatements){
                    buildMethodCall(chainedStatement,jMethodBody,invocation);
                }
                chained=true;
            }
            if(returnType !=null){
                AbstractJClass variableClass = getCm().ref(returnType);
                if(null == variableClass){
                    variableClass=getCm()._class(returnType);
                }
                if(defineAndInitialise){
                    jMethodBody.decl(variableClass,returnVarName,invocation);
                }else {
                    jMethodBody.assign(JExpr.ref(returnVarName),callerExpression);
                }
            }else{
                if(!chained){
                    jMethodBody.add(invocation);
                }
            }
        }
        return jMethodBody;
    }

    private JBlock buildSetterStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody) throws JClassAlreadyExistsException{
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        for(int i=0;i<operandsAndOperators.size();i++){
            LiteralMetadata literal = operandsAndOperators.get(0);
            if(literal.getLiteralType()== LiteralType.VAR_NAME){
                jMethodBody._return(JExpr.ref(literal.getVarName()));
            }else if(literal.getLiteralType()== LiteralType.ARGUMENT_AS_EXPRESSION){
                IJExpression iJExpression =(IJExpression)buildMethodCall(literal.getExpression(),jMethodBody,null).getContents().get(0);
                jMethodBody._return(iJExpression);
                jMethodBody.remove(iJExpression);
            }
        }
        return jMethodBody;
    }

    private JBlock buildReturnStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody) throws JClassAlreadyExistsException{
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        String sourceVarName = null;
        String destinationVarName = null;
        for(int i=0;i<operandsAndOperators.size();i++){
            LiteralMetadata literal = operandsAndOperators.get(i);
            if(literal.getLiteralType() == LiteralType.VAR_NAME){
              jMethodBody._return(JExpr.ref(literal.getVarName()));
            }else if(literal.getLiteralType() == LiteralType.ARGUMENT_AS_EXPRESSION){
                IJExpression iJExpression = (IJExpression)buildMethodCall(literal.getExpression(),jMethodBody,null).getContents().get(0);
                jMethodBody._return(iJExpression);
                jMethodBody.remove(iJExpression);
            }
        }
        return jMethodBody;
    }

    private JBlock buildIfConditionStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        Stack<JVar> operandStack = new Stack<>();
        for(int i=0;i<operandsAndOperators.size();i++){
            LiteralMetadata literal = operandsAndOperators.get(0);
            IJExpression expression=null;
            if(literal.getLiteralType()==LiteralType.OPERAND){
                operandStack.push(resolveLiteralToJVar(literal,jMethodBody));
            }else if( literal.getOperator() == OPERATORS.GT){
                expression = operandStack.pop().gt(operandStack.pop());
            }else if( literal.getOperator() == OPERATORS.LT){
                expression = operandStack.pop().lt(operandStack.pop());
            }else if( literal.getOperator() == OPERATORS.LTEQ){
                expression = operandStack.pop().lte(operandStack.pop());
            }else if( literal.getOperator() == OPERATORS.GTEQ){
                expression = operandStack.pop().gte(operandStack.pop());
            }else if( literal.getOperator() == OPERATORS.NE){
                expression = operandStack.pop().ne(operandStack.pop());
            }else if( literal.getOperator() == OPERATORS.EQ){
                expression = operandStack.pop().eq(operandStack.pop());
            }else if( literal.getOperator() == OPERATORS.INSTANCE_OF){
                expression = operandStack.pop()._instanceof(operandStack.pop().type());
            }
            JConditional condition = jMethodBody._if(expression);
            JBlock innerBlock = new JBlock();
            condition._then().add(generateBlockSegments(logicStatementMetadata.getInnerBlockStatements(),innerBlock));
        }
        return jMethodBody;
    }

    private JBlock buildLoopStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        Stack<JVar> operandStack = new Stack<>();
        for (int i = 0; i < operandsAndOperators.size(); i++) {
            LiteralMetadata literal = operandsAndOperators.get(i);
            if (literal.getLiteralType() == LiteralType.OPERAND) {
                operandStack.push(resolveLiteralToJVar(literal, jMethodBody));
            } else {
                LOOPTYPE loopType = logicStatementMetadata.getLoopType();
                if (loopType == LOOPTYPE.FOREACH) {
                    JForEach jForEach = jMethodBody.forEach(operandStack.pop().type(), "item", operandStack.pop());
                    JBlock innerBlock = new JBlock();
                    jForEach.body().add(generateBlockSegments(logicStatementMetadata.getInnerBlockStatements(), innerBlock));
                } else if (loopType == LOOPTYPE.FOR) {
                    JForLoop forLoop = jMethodBody._for();
                    JVar ivar = forLoop.init(getCm().INT, "i", JExpr.lit(0));
                    forLoop.test(ivar.lt(JExpr.lit(42)));
                    forLoop.update(ivar.assignPlus(JExpr.lit(1)));
                    JBlock innerBlock = new JBlock();
                    forLoop.body().add(generateBlockSegments(logicStatementMetadata.getInnerBlockStatements(), innerBlock));
                }
            }
        }
        return jMethodBody;
    }
    private JBlock buildExceptionStatement(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody) throws JClassAlreadyExistsException {
        List<LiteralMetadata> operandsAndOperators = logicStatementMetadata.getOrderedLiteralsUsed();
        Stack<JVar> operandStack = new Stack<>();
        for (int i = 0; i < operandsAndOperators.size(); i++) {
            LiteralMetadata literal = operandsAndOperators.get(i);
            if (literal.getLiteralType() == LiteralType.OPERAND) {
                operandStack.push(resolveLiteralToJVar(literal, jMethodBody));
            } else {
                jMethodBody._throw(JExpr._new(operandStack.pop().type()));
            }
        }
        return jMethodBody;
    }

    private JBlock buildVariableDeclaration(LogicStatementMetadata logicStatementMetadata,JBlock jMethodBody){
        return jMethodBody;
    }
}
