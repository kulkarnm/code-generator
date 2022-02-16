package org.componentgen.templates.builder.agnosticgateway;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.*;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventTemplateBuilder extends AbstractTemplateBuilder {
    public static final String ACCOUNT_ID_FIELD = "accountId";
    public static final String CARD_NUMBER_FIELD = "cardNumber";
    public static final String CLASS_TYPE_STRING = "String" ;

    public EventTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        List<String> packagesToImport = new ArrayList<>();
        packagesToImport.add(namingContext.getEventClassName());

        ClassMetadata event = new ClassMetadata(namingContext.getEventClassName(), AccessModifier.PUBLIC,null,packagesToImport,null,null,false, CLASSTYPE.CLASS);
        event.setParentMetadata(parentPackage);
        return event;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        List<FieldMetadata> fields = new ArrayList<>();
        FieldMetadata accountId = new FieldMetadata(ACCOUNT_ID_FIELD,AccessModifier.PRIVATE,null,CLASS_TYPE_STRING);
        accountId.setParentMetadata(parentClass);
        fields.add(accountId);

        FieldMetadata cardNumber = new FieldMetadata(CARD_NUMBER_FIELD,AccessModifier.PRIVATE,null,CLASS_TYPE_STRING);
        cardNumber.setParentMetadata(parentClass);
        fields.add(cardNumber);
        return fields;
    }

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass){
        List<MethodMetadata> methods = new ArrayList<>();
        List<FieldMetadata> fields = parentClass.getVariables();
        for(FieldMetadata field : fields){
            MethodMetadata method = buildGetterMethod(field);
            method.setParentClassMetadata(parentClass);
            methods.add(method);
        }
        return methods ;
    }

    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass) {
            ConstructorMetadata constructor = new ConstructorMetadata(parentClass.getClassName(), AccessModifier.PUBLIC, null, "NONE");
            constructor.setParentClassMetadata(parentClass);

            ClassMetadata id = new ClassMetadata(CLASS_TYPE_STRING, null, null, null,null,null, false, CLASSTYPE.CLASS);
            constructor.addToArguments(ACCOUNT_ID_FIELD, id);

            ClassMetadata service = new ClassMetadata(CLASS_TYPE_STRING, null, null, null,null,null, false, CLASSTYPE.CLASS);
            constructor.addToArguments(CARD_NUMBER_FIELD, service);
            buildConstructorLogicMetadata(constructor);
            return constructor;
    }

    private void buildConstructorLogicMetadata(ConstructorMetadata constructorMetadata){
        FieldMetadata instanceVariableId = constructorMetadata.getParentClassMetadata().getVariables().stream().filter(fm ->fm.getName().equals(ACCOUNT_ID_FIELD)).findFirst().get();
        LiteralMetadata targetLiteralId = instanceVariableId.obtainLiteralMetadata();
        targetLiteralId.setPosition(AssignmentPosition.TARGET);
        targetLiteralId.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argumentId = constructorMetadata.getArguments().stream().filter(fm ->fm.getArgumentName().equals(ACCOUNT_ID_FIELD)).findFirst().get();

        LiteralMetadata targetLiteralIdVar = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteralIdVar.setVarName(argumentId.getArgumentName());
        targetLiteralIdVar.setVariableType(VariableType.USER_DEFINED);
        targetLiteralIdVar.setUserDefinedType(argumentId.getArgumentType().getClassName()) ;
        targetLiteralIdVar.setPosition(AssignmentPosition.SOURCE);
        targetLiteralIdVar.setOperator(OPERATORS.ASSIGNMENT);

        LogicStatementMetadata assignmentStatement1 = new LogicStatementMetadata(1);
        assignmentStatement1.setStatementType(STATEMENTTYPE.ASSIGNMENT);
        assignmentStatement1.addToLiteralMetadata(targetLiteralId);
        assignmentStatement1.addToLiteralMetadata(targetLiteralIdVar);
        constructorMetadata.addToBlockStatements(assignmentStatement1);

        FieldMetadata instanceVariable = constructorMetadata.getParentClassMetadata().getVariables().stream().filter(fm ->fm.getName().equals(CARD_NUMBER_FIELD)).findFirst().get();
        LiteralMetadata targetLiteral1 = instanceVariable.obtainLiteralMetadata();
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        targetLiteral1.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argument = constructorMetadata.getArguments().stream().filter(fm ->fm.getArgumentName().equals(CARD_NUMBER_FIELD)).findFirst().get();

        LiteralMetadata targetLiteral2 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral2.setVarName(argument.getArgumentName());
        targetLiteral2.setVariableType(VariableType.USER_DEFINED);
        targetLiteral2.setUserDefinedType(argument.getArgumentType().getClassName()) ;
        targetLiteral2.setPosition(AssignmentPosition.SOURCE);
        targetLiteral2.setOperator(OPERATORS.ASSIGNMENT);

        LogicStatementMetadata assignmentStatement = new LogicStatementMetadata(1);
        assignmentStatement.setStatementType(STATEMENTTYPE.ASSIGNMENT);
        assignmentStatement.addToLiteralMetadata(targetLiteral1);
        assignmentStatement.addToLiteralMetadata(targetLiteral2);
        constructorMetadata.addToBlockStatements(assignmentStatement);
    }
    @Override
    public String getPackageName() { return namingContext.getGatewayPackageName();}
}
