package org.componentgen.templates.builder.service;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.*;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;

public class ServiceImplTemplateBuilder extends AbstractTemplateBuilder {
    public ServiceImplTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        List<String> packagesToImport = new ArrayList<>();
        AnnotationMetadata componentAnnotation = new AnnotationMetadata("Service",null);
        List<AnnotationMetadata> annotations = new ArrayList<>();
        annotations.add(componentAnnotation);
        List<String> implementsFrom = new ArrayList<>();
        implementsFrom.add(namingContext.getServiceClassName());
        return buildClassMetadata(namingContext.getServiceImplClassName(),packagesToImport,annotations,parentPackage,CLASSTYPE.CLASS,implementsFrom);
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        List<FieldMetadata> fields = new ArrayList<>();
        FieldMetadata createDebitCommandService = new FieldMetadata(namingContext.getEventStoreReferenceName(),AccessModifier.PRIVATE,null,namingContext.getEventStoreClassName());
        createDebitCommandService.setParentMetadata(parentClass);
        fields.add(createDebitCommandService);

        fields.add(buildClassLevelLoggerFactory(parentClass,this.componentTemplate));
        return fields;
    }

    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass){
        ConstructorMetadata constructor = new ConstructorMetadata(parentClass.getClassName(),AccessModifier.PUBLIC,null, "NONE");
        constructor.setParentClassMetadata(parentClass);

        ClassMetadata service = new ClassMetadata(namingContext.getEventStoreClassName(),null,null,null,null,null,false,CLASSTYPE.CLASS);
        constructor.addToArguments(namingContext.getEventStoreReferenceName(),service);
        buildConstructorLogicMetadata(constructor);
        return constructor ;
    }

    private void buildConstructorLogicMetadata(ConstructorMetadata constructor){
        FieldMetadata instanceVariable = constructor.getParentClassMetadata().getVariables().stream().filter(fm->fm.getName().equals(namingContext.getEventStoreReferenceName())).findFirst().get();
        LiteralMetadata targetLiteral1 = instanceVariable.obtainLiteralMetadata();
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        targetLiteral1.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argumentId = constructor.getArguments().stream().filter(fm->fm.getArgumentName().equals(namingContext.getEventStoreReferenceName())).findFirst().get();

        LiteralMetadata targetLiteral2 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral2.setVarName(argumentId.getArgumentName());
        targetLiteral2.setVariableType(VariableType.USER_DEFINED);
        targetLiteral2.setUserDefinedType(argumentId.getArgumentType().getClassName());
        targetLiteral2.setPosition(AssignmentPosition.SOURCE);
        targetLiteral2.setOperator(OPERATORS.ASSIGNMENT);

        LogicStatementMetadata assignmentStatement = new LogicStatementMetadata(1);
        assignmentStatement.setStatementType(STATEMENTTYPE.ASSIGNMENT);
        assignmentStatement.addToLiteralMetadata(targetLiteral1);
        assignmentStatement.addToLiteralMetadata(targetLiteral2);
        constructor.addToBlockStatements(assignmentStatement);
    }

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass){
        List<MethodMetadata> methods = new ArrayList<>();

        MethodMetadata businessMethod = new MethodMetadata(namingContext.getMethodName(),AccessModifier.PUBLIC,null,namingContext.getServiceResponseClassName());
        businessMethod.setParentClassMetadata(parentClass);
        buildBusinessMethodAnnotations(businessMethod);
        buildBusinessMethodArguments(businessMethod);
        buildMethodLogicMetadata(businessMethod);
        methods.add(businessMethod);
        return methods ;
    }

    private void buildBusinessMethodAnnotations(MethodMetadata businessMethod){
        AnnotationMetadata override = new AnnotationMetadata("Override",null);
        override.setParentMetadata(businessMethod);
    }

    private void buildBusinessMethodArguments(MethodMetadata businessMethod){
        List<OtherModifier> otherModifiers = new ArrayList<>();
        otherModifiers.add(OtherModifier.FINAL);
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            ClassMetadata methodArgumentType = new ClassMetadata(namingContext.getEventStoreRequestClassName(), null, null, null, null, null, false, CLASSTYPE.CLASS);
            businessMethod.addToArguments(namingContext.getEventStoreRequestReferenceName(), methodArgumentType);
        }else{
            for(LiteralMetadata arg : businessMethodArguments){
                ClassMetadata methodArgumentType = new ClassMetadata(arg.getUserDefinedType(), null, null, null, null, null, false, CLASSTYPE.CLASS);
                businessMethod.addToArguments(arg.getVarName(), methodArgumentType);
            }
        }
    }

    private void buildMethodLogicMetadata(MethodMetadata businessMethod){
        if(namingContext.getApiType() == APITYPE.COMMAND){
            LiteralMetadata targetLiteral1 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral1.setVarName(namingContext.getEventStoreRequestReferenceName());
            targetLiteral1.setUserDefinedType(namingContext.getEventStoreRequestClassName());
            targetLiteral1.setPosition(AssignmentPosition.TARGET);

            LiteralMetadata targetLiteral2 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral2.setVarName(namingContext.getCommandResponseClassReferenceName());
            targetLiteral2.setUserDefinedType(namingContext.getEventStoreResponseClassName());
            targetLiteral2.setPosition(AssignmentPosition.TARGET);

            List<LiteralMetadata> arguments4  = new ArrayList<>();
            arguments4.add(targetLiteral1);
            businessMethod.addToBlockStatements(buildMethodCallStatement(1,namingContext.getEventStoreReferenceName(),namingContext.getMethodName(), targetLiteral2,arguments4,null));

            LiteralMetadata targetLiteral3 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral3.setVarName(namingContext.getWebResponseAttributesReferenceName());
            targetLiteral3.setUserDefinedType(namingContext.getWebResponseAttributesClassName());
            targetLiteral3.setPosition(AssignmentPosition.TARGET);
            businessMethod.addToBlockStatements(buildConstructorCallStatement(2,namingContext.getWebResponseAttributesClassName(),targetLiteral3,null));
            businessMethod.addToBlockStatements(buildReturnStatement(4,targetLiteral3));
        }else {
            LiteralMetadata targetLiteral1 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral1.setVarName(namingContext.getCommandResponseClassReferenceName());
            targetLiteral1.setUserDefinedType(namingContext.getEventStoreResponseClassName());
            targetLiteral1.setPosition(AssignmentPosition.TARGET);

            List<LiteralMetadata> arguments4  = getBusinessMethodArguments();
            businessMethod.addToBlockStatements(buildMethodCallStatement(1,namingContext.getEventStoreReferenceName(),namingContext.getMethodName(), targetLiteral1,arguments4,null));

            LiteralMetadata targetLiteral2 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral2.setVarName(namingContext.getWebResponseAttributesReferenceName());
            targetLiteral2.setUserDefinedType(namingContext.getWebResponseAttributesClassName());
            targetLiteral2.setPosition(AssignmentPosition.TARGET);
            businessMethod.addToBlockStatements(buildConstructorCallStatement(2,namingContext.getWebResponseAttributesClassName(),targetLiteral2,null));
            businessMethod.addToBlockStatements(buildReturnStatement(4,targetLiteral2));
        }

    }

    private LiteralMetadata buildCommandRequest(MethodMetadata businessMethod){
        LiteralMetadata targetLiteral3 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral3.setVarName(namingContext.getCommandClassReferenceName());
        targetLiteral3.setUserDefinedType(namingContext.getCommandClassName());
        targetLiteral3.setPosition(AssignmentPosition.TARGET);
        List<LiteralMetadata> arguments3  = new ArrayList<>();
        LiteralMetadata targetLiteral1 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral1.setVarName("id");
        targetLiteral1.setUserDefinedType("String");
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        LiteralMetadata targetLiteral2 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral2.setVarName(namingContext.getServiceResponseAttributesReferenceName());
        targetLiteral2.setUserDefinedType(namingContext.getServiceResponseAttributesClassName());
        targetLiteral2.setPosition(AssignmentPosition.TARGET);
        arguments3.add(targetLiteral1);
        arguments3.add(targetLiteral2);
        businessMethod.addToBlockStatements(buildConstructorCallStatement(3,namingContext.getCommandClassName(),targetLiteral3,arguments3));
        return targetLiteral3 ;
    }

    public ComponentTemplate getComponentTemplate(){
        return componentTemplate;
    }
    public String getPackageName() {
        return namingContext.getServicePackageName();
    }

}
