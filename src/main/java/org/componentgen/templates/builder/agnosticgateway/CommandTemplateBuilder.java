package org.componentgen.templates.builder.agnosticgateway;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.*;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;

public class CommandTemplateBuilder extends AbstractTemplateBuilder {
    public static final String ID_FIELD = "id" ;
    public static final String CLASS_TYPE_STRING = "String";

    public CommandTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext componentNamingContext){
        super(componentTemplate,componentNamingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            ClassMetadata event = new ClassMetadata(namingContext.getCommandClassName(), AccessModifier.PUBLIC, null, null, null, null, false, CLASSTYPE.CLASS);
            event.setExtendsNarrow(namingContext.getWebRequestAttributesClassName());
            event.setParentMetadata(parentPackage);
            return event;
        }
        return null;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            List<FieldMetadata> fields = new ArrayList<>();
            FieldMetadata accountId = new FieldMetadata(ID_FIELD, AccessModifier.PRIVATE, null, CLASS_TYPE_STRING);
            accountId.setParentMetadata(parentClass);
            fields.add(accountId);

            FieldMetadata serviceResponseAttribute = new FieldMetadata(namingContext.getServiceResponseAttributesReferenceName(), AccessModifier.PRIVATE, null, namingContext.getServiceResponseAttributesClassName());
            serviceResponseAttribute.setParentMetadata(parentClass);
            fields.add(serviceResponseAttribute);

            return fields;
        }
        return null;
    }

    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass) {
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            ConstructorMetadata constructor = new ConstructorMetadata(parentClass.getClassName(), AccessModifier.PUBLIC, null, "NONE");
            constructor.setParentClassMetadata(parentClass);

            ClassMetadata id = new ClassMetadata(CLASS_TYPE_STRING, null, null, null,null,null, false, CLASSTYPE.CLASS);
            constructor.addToArguments(ID_FIELD, id);

            ClassMetadata service = new ClassMetadata(namingContext.getServiceResponseAttributesClassName(), null, null, null,null,null, false, CLASSTYPE.CLASS);
            constructor.addToArguments(namingContext.getServiceResponseAttributesReferenceName(), service);


            buildConstructorLogicMetadata(constructor);
            return constructor;
        }
        return null;
    }

    private void buildConstructorLogicMetadata(ConstructorMetadata constructorMetadata){
        FieldMetadata instanceVariableId = constructorMetadata.getParentClassMetadata().getVariables().stream().filter(fm ->fm.getName().equals(ID_FIELD)).findFirst().get();
        LiteralMetadata targetLiteralId = instanceVariableId.obtainLiteralMetadata();
        targetLiteralId.setPosition(AssignmentPosition.TARGET);
        targetLiteralId.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argumentId = constructorMetadata.getArguments().stream().filter(fm ->fm.getArgumentName().equals(ID_FIELD)).findFirst().get();

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

        FieldMetadata instanceVariable = constructorMetadata.getParentClassMetadata().getVariables().stream().filter(fm ->fm.getName().equals(namingContext.getServiceResponseAttributesReferenceName())).findFirst().get();
        LiteralMetadata targetLiteral1 = instanceVariable.obtainLiteralMetadata();
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        targetLiteral1.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argument = constructorMetadata.getArguments().stream().filter(fm ->fm.getArgumentName().equals(namingContext.getServiceResponseAttributesReferenceName())).findFirst().get();

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
    protected  List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass) {
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            List<MethodMetadata> methods = new ArrayList<>();
            MethodMetadata getId = new MethodMetadata("getId", AccessModifier.PUBLIC, null, CLASS_TYPE_STRING);
            getId.setParentClassMetadata(parentClass);
            buildGetterStatement(ID_FIELD,CLASS_TYPE_STRING,getId);

            MethodMetadata setId = new MethodMetadata("setId", AccessModifier.PUBLIC, null, "void");
            setId.setParentClassMetadata(parentClass);

            ClassMetadata argumentId = new ClassMetadata(CLASS_TYPE_STRING, null,null,null,null,null,false,CLASSTYPE.CLASS);
            setId.addToArguments(ID_FIELD,argumentId);
            buildMethodLogicMetadataForSetterMethod(setId,ID_FIELD,argumentId.getClassName());

            MethodMetadata getAttributes = new MethodMetadata("getAttributes", AccessModifier.PUBLIC, null, namingContext.getWebRequestAttributesClassName());
            getAttributes.setParentClassMetadata(parentClass);
            buildGetterStatement(namingContext.getWebRequestAttributesReferenceName(),namingContext.getWebRequestAttributesClassName(),getAttributes);

            MethodMetadata setAttributes = new MethodMetadata("setAttributes", AccessModifier.PUBLIC, null, "void");
            setAttributes.setParentClassMetadata(parentClass);
            ClassMetadata argument = new ClassMetadata(namingContext.getWebRequestAttributesClassName(), null,null,null,null,null,false,CLASSTYPE.CLASS);
            setAttributes.addToArguments(namingContext.getWebRequestAttributesReferenceName(),argument);
            buildMethodLogicMetadataForSetterMethod(setAttributes,namingContext.getWebRequestAttributesReferenceName(),argument.getClassName());
            methods.add(getAttributes);
            methods.add(setAttributes);
            return methods;
        }
        return null;
    }

    @Override
    public String getPackageName() { return namingContext.getGatewayPackageName();}
}
