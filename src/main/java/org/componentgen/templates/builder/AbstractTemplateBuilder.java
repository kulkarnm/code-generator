package org.componentgen.templates.builder;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.*;
import org.componentgen.templates.ComponentTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTemplateBuilder implements TemplateBuilder{
    protected ComponentTemplate componentTemplate;
    protected ComponentNamingContext namingContext;
    protected List<LiteralMetadata> businessMethodArguments;

    public AbstractTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext) {
        this.componentTemplate = componentTemplate;
        this.namingContext = namingContext;
    }

    protected PackageMetadata buildPackageMetadata(ComponentTemplate componentTemplate){
        try{
            PackageMetadata existingPackageMetadata = componentTemplate.getPackageMetadata();
            if( null == existingPackageMetadata || !existingPackageMetadata.getPackageName().equals(this.getPackageName())){
                PackageMetadata packageMetadata = new PackageMetadata(this.getPackageName());
                componentTemplate.setPackageMetadata(packageMetadata);
                return packageMetadata;
            }else {
                componentTemplate.setPackageMetadata(existingPackageMetadata);
            }
            return existingPackageMetadata;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }
    protected ClassMetadata buildClassMetadata(String className, List<String> packagesToImport, List<AnnotationMetadata> annotations, PackageMetadata parent, CLASSTYPE classtype,List<String> implementsFrom) {
        ClassMetadata classMetadata = new ClassMetadata(className, AccessModifier.PUBLIC,null,packagesToImport,null,implementsFrom,false,classtype);
        classMetadata.setParentMetadata(parent);

        for(AnnotationMetadata annotation : annotations){
            annotation.setParentMetadata(classMetadata);
        }
        return classMetadata;
    }

    protected abstract ClassMetadata buildClassMetadata(PackageMetadata packageMetadata);

    @Override
    public ComponentTemplate buildTemplate(ComponentTemplate componentTemplate){
        PackageMetadata packageMetadata = buildPackageMetadata(componentTemplate);
        ClassMetadata classMetadata = buildClassMetadata(packageMetadata);
        buildFieldsMetadata(classMetadata);
        buildConstructorMetadata(classMetadata);
        buildMethodMetadata(classMetadata);
        return componentTemplate;
    }

    protected abstract List<FieldMetadata> buildFieldsMetadata(ClassMetadata classMetadata);

    protected  ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass){
        ConstructorMetadata constructor = new ConstructorMetadata(parentClass.getClassName(),AccessModifier.PUBLIC,null, "NONE") ;
        constructor.setParentClassMetadata(parentClass);
        List<FieldMetadata> fields = parentClass.getVariables();
        for(FieldMetadata field : fields){
            constructor.addToArguments (field.getName(), new ClassMetadata(field.getCompositeType(),AccessModifier.PUBLIC,null,null,null,null,false,CLASSTYPE.CLASS )) ;
        }
        buildConstructorLogicMetadata(constructor);
        return  constructor;
    }

    private void buildConstructorLogicMetadata(ConstructorMetadata constructor) {
        List<ArgumentMetadata> arguments = constructor.getArguments();
        Map<LiteralMetadata,LiteralMetadata> literalsInExpression = new HashMap<>();
        for(ArgumentMetadata argument : arguments){
            LiteralMetadata literal = new LiteralMetadata(LiteralType.ARGUMENT_NAME);
            literal.setVarName(argument.getArgumentName());
            literal.setVariableType(VariableType.USER_DEFINED);
            literal.setUserDefinedType(argument.getArgumentType().getClassName()) ;
            literalsInExpression.put(literal,literal);
        }
        buildSetterStatements(constructor,literalsInExpression);
    }

    protected abstract List<MethodMetadata> buildMethodMetadata(ClassMetadata classMetadata);

    protected void buildLogDebugStatement(MethodMetadata businessMethod) {
        LiteralMetadata literal23 = new LiteralMetadata(LiteralType.ARGUMENT_NAME);
        literal23.setVarName("\"" + businessMethod.getMethodName() + " param\"");
        List<LiteralMetadata> arguments2 = new ArrayList<>();
        arguments2.add(literal23);
        businessMethod.addToBlockStatements(buildMethodCallStatement(2,"LOG","debug",null,arguments2,null));

    }

    protected void buildLogInfoStatement(MethodMetadata businessMethod) {
        LiteralMetadata literal23 = new LiteralMetadata(LiteralType.ARGUMENT_NAME);
        literal23.setVarName("\"" + businessMethod.getMethodName() + " Id={}\"");
        List<LiteralMetadata> arguments2 = new ArrayList<>();
        arguments2.add(literal23);
        businessMethod.addToBlockStatements(buildMethodCallStatement(2,"LOG","info",null,arguments2,null));
    }

    protected FieldMetadata buildClassLevelLoggerFactory(ClassMetadata parentClass, ComponentTemplate componentTemplate){
        List<OtherModifier> otherModifiers = new ArrayList<>();
        otherModifiers.add(OtherModifier.STATIC);
        otherModifiers.add(OtherModifier.FINAL);

        FieldMetadata LOG = new FieldMetadata("LOG", AccessModifier.PUBLIC, otherModifiers, "Logger");

        LiteralMetadata literal = new LiteralMetadata(LiteralType.ARGUMENT_NAME);
        literal.setUserDefinedType(LOG.getCompositeType());
        List<LiteralMetadata> arguments2 = new ArrayList<>();
        arguments2.add(literal);

        LiteralMetadata targetLiteral = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral.setVarName(LOG.getName());
        targetLiteral.setUserDefinedType(LOG.getCompositeType());
        targetLiteral.setPosition(AssignmentPosition.TARGET);
        LOG.setAssignedExpression(componentTemplate.buildMethodCallStatement(1,"LoggerFactory","getLogger",targetLiteral,arguments2));
        LOG.setParentMetadata(parentClass);
        return LOG ;
    }
    public LogicStatementMetadata buildSuperCallStatement(int sequence,List<LiteralMetadata> arguments){
        LogicStatementMetadata statement = new LogicStatementMetadata(sequence);
        statement.setStatementType(STATEMENTTYPE.SUPER_CALL);
        if(null !=arguments && !arguments.isEmpty()) {
            for(LiteralMetadata literal : arguments){
                if(literal.getLiteralType() != LiteralType.ARGUMENT_NAME){
                    statement.reuseLiteralMetadata(literal,LiteralType.ARGUMENT_NAME);
                }else{
                    statement.addToLiteralMetadata(literal);
                }
            }
        }
        return statement;
    }

    public LogicStatementMetadata buildMethodCallStatement(int sequence, String callerName, String methodName,LiteralMetadata targetVariable,List<LiteralMetadata> arguments, List<LogicStatementMetadata> chainedCallStatements) {
        LogicStatementMetadata statement = new LogicStatementMetadata(sequence);
        statement.setStatementType(STATEMENTTYPE.METHOD_CALL);

        if(null != targetVariable){
            if(targetVariable.getPosition() != AssignmentPosition.TARGET){
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
                if(literal.getLiteralType() == LiteralType.ARGUMENT_AS_EXPRESSION){
                 statement.addToLiteralMetadata(literal);
                }else {
                    if (literal.getLiteralType() != LiteralType.ARGUMENT_NAME) {
                        statement.reuseLiteralMetadata(literal, LiteralType.ARGUMENT_NAME);
                    } else {
                        statement.addToLiteralMetadata(literal);
                    }
                }
            }
        }
        statement.setChainedCallStatements(chainedCallStatements);
        return statement;
    }

    public LogicStatementMetadata buildConstructorCallStatement(int sequence, String classOrTypeName, LiteralMetadata targetVariable,List<LiteralMetadata> arguments) {
        LogicStatementMetadata statement = new LogicStatementMetadata(sequence);
        statement.setStatementType(STATEMENTTYPE.CONSTRUCTOR_CALL);

        if(null != targetVariable){
            if(targetVariable.getPosition() != AssignmentPosition.TARGET){
                targetVariable.setPosition(AssignmentPosition.TARGET);
            }
            statement.addToLiteralMetadata(targetVariable);
        }

        LiteralMetadata literal3 = new LiteralMetadata(LiteralType.CONSTRUCTOR_TYPE_NAME);
        literal3.setUserDefinedType(classOrTypeName);
        statement.addToLiteralMetadata(literal3);

        if( null != arguments && ! arguments.isEmpty()){
            for(LiteralMetadata literal : arguments){
                if(literal.getLiteralType() == LiteralType.ARGUMENT_AS_EXPRESSION){
                    statement.addToLiteralMetadata(literal);
                }else {
                    if (literal.getLiteralType() != LiteralType.ARGUMENT_NAME) {
                        statement.reuseLiteralMetadata(literal, LiteralType.ARGUMENT_NAME);
                    } else {
                        statement.addToLiteralMetadata(literal);
                    }
                }
            }
        }
        return statement;
    }

    protected void buildMethodLogicMetadataForSetterMethod(MethodMetadata businessMethod,String variableName,String variableType){
        LiteralMetadata targetLiteral1 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral1.setVariableType(VariableType.USER_DEFINED);
        targetLiteral1.setVarName(variableName);
        targetLiteral1.setUserDefinedType(variableType);
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        targetLiteral1.setOperator(OPERATORS.ASSIGNMENT);


        LiteralMetadata targetLiteral2 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral2.setVariableType(VariableType.USER_DEFINED);
        targetLiteral2.setVarName(variableName);
        targetLiteral2.setUserDefinedType(variableType);
        targetLiteral2.setPosition(AssignmentPosition.SOURCE);
        targetLiteral2.setOperator(OPERATORS.ASSIGNMENT);

        LogicStatementMetadata assignmentStatement = new LogicStatementMetadata(1);
        assignmentStatement.setStatementType(STATEMENTTYPE.SETTER_STATEMENT);
        assignmentStatement.addToLiteralMetadata(targetLiteral1);
        assignmentStatement.addToLiteralMetadata(targetLiteral2);
        businessMethod.addToBlockStatements(assignmentStatement);
    }

    public void buildGetterStatement(String variableName,String variableType,MethodMetadata methodMetadata){
        LiteralMetadata targetLiteral1 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral1.setVariableType(VariableType.USER_DEFINED);
        targetLiteral1.setVarName(variableName);
        targetLiteral1.setUserDefinedType(variableType);
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        methodMetadata.addToBlockStatements(buildReturnStatement(1,targetLiteral1));
    }

    public MethodMetadata buildGetterMethod(FieldMetadata field){
        LiteralMetadata targetLiteral1 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral1.setVariableType(VariableType.USER_DEFINED);
        targetLiteral1.setVarName(field.getName());
        targetLiteral1.setUserDefinedType(field.getCompositeType());
        targetLiteral1.setPosition(AssignmentPosition.TARGET);

        List<String> getterAndSetterNames = namingContext.buildGetterAndSetterNamesFromField(field);
        MethodMetadata method = new MethodMetadata(getterAndSetterNames.get(0),AccessModifier.PUBLIC,null,field.getCompositeType());
        method.addToBlockStatements(buildReturnStatement(1,targetLiteral1));
        return method;
    }

    public LogicStatementMetadata buildReturnStatement(int sequence,LiteralMetadata targetVariable){
        LogicStatementMetadata statement = new LogicStatementMetadata(sequence);
        statement.setStatementType(STATEMENTTYPE.RETURN_STATEMENT);
        statement.addToLiteralMetadata(targetVariable);
        return statement;
    }

    public void buildAssignmentStatements(TypeMetadata methodOrConstructor, Map<LiteralMetadata,LiteralMetadata> literalsInExpression) {
        for(Map.Entry<LiteralMetadata,LiteralMetadata> assignmentExpression : literalsInExpression.entrySet()){
         LiteralMetadata source = assignmentExpression.getKey();
            LiteralMetadata destination = assignmentExpression.getValue();
            source.setPosition(AssignmentPosition.SOURCE);
            source.setOperator(OPERATORS.ASSIGNMENT);

            destination.setPosition(AssignmentPosition.TARGET);

            LogicStatementMetadata assignmentStatement = new LogicStatementMetadata(1);
            assignmentStatement.setStatementType(STATEMENTTYPE.ASSIGNMENT);
            assignmentStatement.addToLiteralMetadata(source);
            assignmentStatement.addToLiteralMetadata(destination);
            methodOrConstructor.addToBlockStatements(assignmentStatement);

        }
    }

    public void buildSetterStatements(TypeMetadata methodOrConstructor, Map<LiteralMetadata,LiteralMetadata> literalsInExpression) {
        for(Map.Entry<LiteralMetadata,LiteralMetadata> assignmentExpression : literalsInExpression.entrySet()){
            LiteralMetadata source = assignmentExpression.getKey();
            LiteralMetadata destination = assignmentExpression.getValue();
            source.setPosition(AssignmentPosition.SOURCE);
            source.setOperator(OPERATORS.ASSIGNMENT);

            destination.setPosition(AssignmentPosition.TARGET);

            LogicStatementMetadata assignmentStatement = new LogicStatementMetadata(1);
            assignmentStatement.setStatementType(STATEMENTTYPE.SETTER_STATEMENT);
            assignmentStatement.addToLiteralMetadata(source);
            assignmentStatement.addToLiteralMetadata(destination);
            methodOrConstructor.addToBlockStatements(assignmentStatement);

        }
    }

    public MethodMetadata buildSetterMethod(FieldMetadata field){
        LiteralMetadata source = field.obtainLiteralMetadata();
        LiteralMetadata destination = field.obtainLiteralMetadata();
        source.setPosition(AssignmentPosition.TARGET);
        source.setOperator(OPERATORS.ASSIGNMENT);
        destination.setPosition(AssignmentPosition.TARGET);

        LogicStatementMetadata assignmentStatement = new LogicStatementMetadata(1);
        assignmentStatement.setStatementType(STATEMENTTYPE.SETTER_STATEMENT);
        assignmentStatement.addToLiteralMetadata(source);
        assignmentStatement.addToLiteralMetadata(destination);

        List<String> getterAndSetterNames = namingContext.buildGetterAndSetterNamesFromField(field);
        MethodMetadata method = new MethodMetadata(getterAndSetterNames.get(1),AccessModifier.PUBLIC,null,"void");
        method.addToBlockStatements(assignmentStatement);
        return method;
    }

    public List<LiteralMetadata> getBusinessMethodArguments() {
        return businessMethodArguments;
    }

    public void setBusinessMethodArguments(List<LiteralMetadata> businessMethodArguments) {
        this.businessMethodArguments = businessMethodArguments;
    }

    public abstract String getPackageName();
}
