package org.componentgen.templates.builder.service;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.*;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;

import java.util.ArrayList;
import java.util.List;

public class ResponseTemplateBuilder extends AbstractTemplateBuilder {
    private static final String ID_FIELD = "id" ;
    private static final String CLASS_TYPE_STRING = "String" ;

    public ResponseTemplateBuilder(ComponentTemplate componentTemplate,ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        List<String> packagesToImportForCommandResponse = new ArrayList<>();
        packagesToImportForCommandResponse.add(namingContext.getServiceResponseClassName());
        ClassMetadata commandResponse = new ClassMetadata(namingContext.getServiceResponseClassName(), AccessModifier.PUBLIC,null,packagesToImportForCommandResponse,null,null,false, CLASSTYPE.CLASS);
        commandResponse.setParentMetadata(parentPackage);
        return commandResponse;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        List<FieldMetadata> fields = new ArrayList<>();
        FieldMetadata id = new FieldMetadata("id",AccessModifier.PRIVATE,null,"String");
        id.setParentMetadata(parentClass);
        fields.add(id);

        FieldMetadata responseAttributes = new FieldMetadata(namingContext.getWebResponseAttributesReferenceName(),AccessModifier.PRIVATE,null,namingContext.getWebResponseAttributesClassName());
        responseAttributes.setParentMetadata(parentClass);
        fields.add(responseAttributes);
        return fields;
    }


    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass){
        List<MethodMetadata> methods = new ArrayList<>();
        List<FieldMetadata> fields = parentClass.getVariables();
        for(FieldMetadata field : fields){
            if("id".equals(field.getName())){
                MethodMetadata method = buildGetterMethod(field);
                method.setParentClassMetadata(parentClass);
                methods.add(method);
            }
        }

        MethodMetadata getAttributes = new MethodMetadata("getAttributes",AccessModifier.PUBLIC,null,namingContext.getWebResponseAttributesReferenceName());
        getAttributes.setParentClassMetadata(parentClass);
        buildGetterStatement(namingContext.getWebRequestAttributesReferenceName(),namingContext.getWebRequestAttributesClassName(),getAttributes);
        methods.add(getAttributes);
        return methods ;
    }
    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass){
        ConstructorMetadata constructor = new ConstructorMetadata(parentClass.getClassName(),AccessModifier.PUBLIC,null, "NONE");
        constructor.setParentClassMetadata(parentClass);

        ClassMetadata id = new ClassMetadata(CLASS_TYPE_STRING,null,null,null,null,null,false,CLASSTYPE.CLASS);
        constructor.addToArguments(ID_FIELD,id);
        ClassMetadata service = new ClassMetadata(namingContext.getWebResponseAttributesClassName(),null,null,null,null,null,false,CLASSTYPE.CLASS);
        constructor.addToArguments(namingContext.getWebResponseAttributesReferenceName(),service);
        buildConstructorLogicMetadata(constructor);
        return constructor ;
    }

    private void buildConstructorLogicMetadata(ConstructorMetadata constructor){
        FieldMetadata instanceVariableId = constructor.getParentClassMetadata().getVariables().stream().filter(fm->fm.getName().equals(ID_FIELD)).findFirst().get();
        LiteralMetadata targetLiteralId = instanceVariableId.obtainLiteralMetadata();
        targetLiteralId.setPosition(AssignmentPosition.TARGET);
        targetLiteralId.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argumentId = constructor.getArguments().stream().filter(fm->fm.getArgumentName().equals(ID_FIELD)).findFirst().get();

        LiteralMetadata targetLiteralIdVar = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteralIdVar.setVarName(argumentId.getArgumentName());
        targetLiteralIdVar.setVariableType(VariableType.USER_DEFINED);
        targetLiteralIdVar.setUserDefinedType(argumentId.getArgumentType().getClassName());
        targetLiteralIdVar.setPosition(AssignmentPosition.SOURCE);
        targetLiteralIdVar.setOperator(OPERATORS.ASSIGNMENT);

        LogicStatementMetadata assignmentStatement1 = new LogicStatementMetadata(1);
        assignmentStatement1.setStatementType(STATEMENTTYPE.ASSIGNMENT);
        assignmentStatement1.addToLiteralMetadata(targetLiteralId);
        assignmentStatement1.addToLiteralMetadata(targetLiteralIdVar);
        constructor.addToBlockStatements(assignmentStatement1);

        FieldMetadata instanceVariable = constructor.getParentClassMetadata().getVariables().stream().filter(fm->fm.getName().equals(namingContext.getWebResponseAttributesReferenceName())).findFirst().get();
        LiteralMetadata targetLiteral1 = instanceVariable.obtainLiteralMetadata();
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        targetLiteral1.setOperator(OPERATORS.ASSIGNMENT);
        ArgumentMetadata argument = constructor.getArguments().stream().filter(fm->fm.getArgumentName().equals(namingContext.getWebResponseAttributesReferenceName())).findFirst().get();

        LiteralMetadata targetLiteral2 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral2.setVarName(argument.getArgumentName());
        targetLiteral2.setVariableType(VariableType.USER_DEFINED);
        targetLiteral2.setUserDefinedType(argument.getArgumentType().getClassName());
        targetLiteral2.setPosition(AssignmentPosition.SOURCE);
        targetLiteral2.setOperator(OPERATORS.ASSIGNMENT);

        LogicStatementMetadata assignmentStatement = new LogicStatementMetadata(2);
        assignmentStatement.setStatementType(STATEMENTTYPE.ASSIGNMENT);
        assignmentStatement.addToLiteralMetadata(targetLiteral1);
        assignmentStatement.addToLiteralMetadata(targetLiteral2);
        constructor.addToBlockStatements(assignmentStatement);
    }

    @Override
    public String getPackageName() { return namingContext.getServicePackageName();}
}
