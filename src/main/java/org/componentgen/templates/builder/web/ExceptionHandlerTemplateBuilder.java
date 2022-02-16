package org.componentgen.templates.builder.web;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.*;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExceptionHandlerTemplateBuilder extends AbstractTemplateBuilder {
    public static final String ANNOTATION_PARAM_KEY = "NONE" ;

    public ExceptionHandlerTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext componentNamingContext){
        super(componentTemplate,componentNamingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage) {
        List<String> packagesToImportFromController = new ArrayList<>();
        AnnotationMetadata controllerAdvice = new AnnotationMetadata("RestControllerAdvice", null);
        Map<String,List<AnnotationParam>> propertySource = new HashMap<>();
        List<AnnotationParam> annotationParamList = new ArrayList<>();
        AnnotationParam annotationParam = new AnnotationParam("{classpath:payment-exception-mapping.properties}", VariableType.STRING);
        annotationParamList.add(annotationParam);
        propertySource.put("NONE",annotationParamList);
        AnnotationMetadata propertySourceAnnotation = new AnnotationMetadata("PropertySource",propertySource);
        List<AnnotationMetadata> annotations = new ArrayList<>();
        annotations.add(controllerAdvice);
        annotations.add(propertySourceAnnotation);
        return buildClassMetadata(namingContext.getExceptionHandlerName(),packagesToImportFromController,annotations,parentPackage, CLASSTYPE.CLASS,null);
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass) {
        List<FieldMetadata> fields = new ArrayList<>();
        fields.add(buildClassLevelLoggerFactory(parentClass,this.componentTemplate));
        FieldMetadata environmentField = new FieldMetadata("environment", AccessModifier.PRIVATE,null, "Environment");
        environmentField.setParentMetadata(parentClass);
        fields.add(environmentField);

        FieldMetadata genericSystemExceptionCodeDesc = getFieldMetadata(parentClass,"String","genericSystemExceptionCodeDesc");

        AnnotationParam annotationParam0 = new AnnotationParam("${generic.system.exception.description}",VariableType.STRING);
        List<AnnotationParam> annotationParamsValues0 = new ArrayList<>();
        annotationParamsValues0.add(annotationParam0);

        setAnnotation(genericSystemExceptionCodeDesc,"NONE",annotationParamsValues0,"Value");
        fields.add(genericSystemExceptionCodeDesc);

        FieldMetadata genericSystemExceptionCode = getFieldMetadata(parentClass,"String","genericSystemExceptionCode");
        AnnotationParam annotationParam5 = new AnnotationParam("${generic.system.exception.code}",VariableType.STRING);
        List<AnnotationParam> annotationParamsValues5 = new ArrayList<>();
        annotationParamsValues5.add(annotationParam5);
        setAnnotation(genericSystemExceptionCode,"NONE",annotationParamsValues5,"Value");
        fields.add(genericSystemExceptionCode);

        FieldMetadata genericSystemExceptionDetails = getFieldMetadata(parentClass,"String","genericSystemExceptionDetails");
        AnnotationParam annotationParam1 = new AnnotationParam("${generic.system.exception.details}",VariableType.STRING);
        List<AnnotationParam> annotationParamsValues1 = new ArrayList<>();
        annotationParamsValues1.add(annotationParam1);
        setAnnotation(genericSystemExceptionDetails,"NONE",annotationParamsValues1,"Value");
        fields.add(genericSystemExceptionDetails);

        FieldMetadata resourceNotFoundExceptionDesc = getFieldMetadata(parentClass,"String","resourceNotFoundExceptionDesc");
        AnnotationParam annotationParam2 = new AnnotationParam("${resource.not.found.exception.description}",VariableType.STRING);
        List<AnnotationParam> annotationParamsValues2 = new ArrayList<>();
        annotationParamsValues2.add(annotationParam2);
        setAnnotation(resourceNotFoundExceptionDesc,"NONE",annotationParamsValues2,"Value");
        fields.add(resourceNotFoundExceptionDesc);

        FieldMetadata resourceNotFoundExceptionCode = getFieldMetadata(parentClass,"String","resourceNotFoundExceptionCode");
        AnnotationParam annotationParam3 = new AnnotationParam("${resource.not.found.exception.code}",VariableType.STRING);
        List<AnnotationParam> annotationParamsValues3 = new ArrayList<>();
        annotationParamsValues3.add(annotationParam3);
        setAnnotation(resourceNotFoundExceptionCode,"NONE",annotationParamsValues3,"Value");
        fields.add(resourceNotFoundExceptionCode);

        FieldMetadata resourceNotFoundExceptionDetails = getFieldMetadata(parentClass,"String","resourceNotFoundExceptionDetails");
        AnnotationParam annotationParam4 = new AnnotationParam("${resource.not.found.exception.details}",VariableType.STRING);
        List<AnnotationParam> annotationParamsValues4 = new ArrayList<>();
        annotationParamsValues4.add(annotationParam4);
        setAnnotation(resourceNotFoundExceptionDetails,"NONE",annotationParamsValues4,"Value");
        fields.add(resourceNotFoundExceptionDetails);
        return fields;
    }


    private FieldMetadata getFieldMetadata(ClassMetadata parentClass,String fieldType,String fieldName){
        FieldMetadata fieldMetadata = new FieldMetadata(fieldName,AccessModifier.PRIVATE,null,fieldType);
        fieldMetadata.setParentMetadata(parentClass);
        return fieldMetadata;
    }

    private void setAnnotation(TypeMetadata typeMetadata, String annotationParamKey,List<AnnotationParam> annotationParamValue, String annotationName){
        Map<String,List<AnnotationParam>> annotationParamsValue = null;
        if(!(null == annotationParamKey || "".equals(annotationParamKey))){
            annotationParamsValue = new HashMap<>();
            annotationParamsValue.put(annotationParamKey,annotationParamValue);
        }
        AnnotationMetadata value = new AnnotationMetadata(annotationName,annotationParamsValue);
        value.setParentMetadata(typeMetadata);
    }

    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass) {
        ConstructorMetadata constructor = new ConstructorMetadata(parentClass.getClassName(),AccessModifier.PUBLIC,null,"NONE");
        constructor.setParentClassMetadata(parentClass);
        ClassMetadata service = new ClassMetadata("Environment", null,null,null,null,null,false,CLASSTYPE.CLASS);
        constructor.addToArguments("environment",service);
        buildConstructorLogicMetadata(constructor);
        return null;
    }

    private void buildConstructorLogicMetadata(ConstructorMetadata constructorMetadata){
        FieldMetadata instanceVariable = constructorMetadata.getParentClassMetadata().getVariables().stream().filter(fm ->fm.getName().equals("environment")).findFirst().get();
        LiteralMetadata targetLiteral1 = instanceVariable.obtainLiteralMetadata();
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        targetLiteral1.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argument = constructorMetadata.getArguments().stream().filter(fm ->fm.getArgumentName().equals("environment")).findFirst().get();

        LiteralMetadata targetLiteralErrorCode = getLiteralMetadata(LiteralType.VAR_NAME,argument.getArgumentName(),argument.getArgumentType().getClassName(),AssignmentPosition.SOURCE,VariableType.USER_DEFINED,OPERATORS.ASSIGNMENT);

        LogicStatementMetadata assignmentStatement = getLogicStatementMetadata(1,STATEMENTTYPE.ASSIGNMENT,targetLiteral1,targetLiteralErrorCode);
        constructorMetadata.addToBlockStatements(assignmentStatement);
    }

    private LiteralMetadata getLiteralMetadata(LiteralType literalType,String varName,String userDefinedType,AssignmentPosition assignmentPosition,VariableType setVariableType, OPERATORS operators){
        LiteralMetadata literal = new LiteralMetadata(literalType);
        literal.setVarName(varName);
        literal.setVariableType(setVariableType);
        literal.setUserDefinedType(userDefinedType) ;
        literal.setPosition(assignmentPosition);
        literal.setOperator(operators);
        return literal;
    }
    @Override
    protected  List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass) {
        List<MethodMetadata> methods = new ArrayList<>();
        MethodMetadata businessMethod = new MethodMetadata("handle",AccessModifier.PUBLIC,null,"ResponseEntity<Object>") ;
        businessMethod.setParentClassMetadata(parentClass);
        buildBusinessMethodAnnotations(businessMethod);
        buildBusinessMethodArguments(businessMethod);
        buildMethodLogicMetadata(businessMethod);
        methods.add(businessMethod);

        MethodMetadata handleNotFoundException = new MethodMetadata("handleNotFoundException",AccessModifier.PUBLIC,null,"ResponseEntity<Object>") ;
        handleNotFoundException.setParentClassMetadata(parentClass);
        buildBusinessMethodAnnotationsForHandleNotFoundException(handleNotFoundException);
        buildMethodLogicMetadataForHandlerNotFoundException(handleNotFoundException);
        methods.add(handleNotFoundException);


        MethodMetadata businessMethodBuildGenerateResponseId = new MethodMetadata("generateResponseId",AccessModifier.PRIVATE,null,"String") ;
        businessMethodBuildGenerateResponseId.setParentClassMetadata(parentClass);
        buildGenerateResponseId(businessMethodBuildGenerateResponseId);
        methods.add(businessMethodBuildGenerateResponseId);

        MethodMetadata createBuildResponse = new MethodMetadata("buildResponse",AccessModifier.PRIVATE,null,"ResponseEntity<Object>") ;
        createBuildResponse.setParentClassMetadata(parentClass);
        createBuildResponseMethod(createBuildResponse);
        buildBusinessMethodArgumentsForBuildResponse(createBuildResponse);
        methods.add(createBuildResponse);
        return methods ;

    }
    private void buildBusinessMethodAnnotationsForHandleNotFoundException(MethodMetadata businessMethod){
        /*AnnotationMetadata override = new AnnotationMetadata("Override",null);
        override.setParentMetadata(businessMethod);*/
        AnnotationParam param0 = new AnnotationParam("com.barclaycard.digital.core.Exception.NotFoundException", VariableType.CLASS);
        List<AnnotationParam> annotationParamValues0 = new ArrayList<>();
        annotationParamValues0.add(param0);
        setAnnotation(businessMethod,ANNOTATION_PARAM_KEY,annotationParamValues0, "ExceptionHandler");
    }
    private void buildBusinessMethodAnnotations(MethodMetadata businessMethod){
        AnnotationMetadata override = new AnnotationMetadata("Override",null);
        override.setParentMetadata(businessMethod);
        AnnotationParam param0 = new AnnotationParam("java.lang.Exception", VariableType.CLASS);
        AnnotationParam param1 = new AnnotationParam("java.lang.RuntimeException", VariableType.CLASS);
        List<AnnotationParam> annotationParamValues0 = new ArrayList<>();
        annotationParamValues0.add(param0);
        annotationParamValues0.add(param1);
        setAnnotation(businessMethod,ANNOTATION_PARAM_KEY,annotationParamValues0, "ExceptionHandler");
    }

    private void buildBusinessMethodArgumentsForBuildResponse(MethodMetadata businessMethod){
        ClassMetadata methodArgumentType = new ClassMetadata("HttpStatus", null,null,null,null,null,false,CLASSTYPE.CLASS);
        businessMethod.addToArguments("status",methodArgumentType);

        ClassMetadata methodArgumentTypeErrorCode = new ClassMetadata("String", null,null,null,null,null,false,CLASSTYPE.CLASS);
        businessMethod.addToArguments("errorCode",methodArgumentTypeErrorCode);

        ClassMetadata methodArgumentTypeErrorMessage = new ClassMetadata("String", null,null,null,null,null,false,CLASSTYPE.CLASS);
        businessMethod.addToArguments("errorMessage",methodArgumentTypeErrorMessage);

        ClassMetadata methodArgumentTypeDetail = new ClassMetadata("String", null,null,null,null,null,false,CLASSTYPE.CLASS);
        businessMethod.addToArguments("detail",methodArgumentTypeDetail);

        ClassMetadata methodArgumentTypeId = new ClassMetadata("String", null,null,null,null,null,false,CLASSTYPE.CLASS);
        businessMethod.addToArguments("id",methodArgumentTypeId);


    }

    private void buildBusinessMethodArguments(MethodMetadata businessMethod){
        List<OtherModifier> otherModifiers = new ArrayList<>();
        otherModifiers.add(OtherModifier.FINAL);

        ClassMetadata methodArgumentType = new ClassMetadata("Exception", null,otherModifiers,null,null,null,false,CLASSTYPE.CLASS);
        businessMethod.addToArguments("exception",methodArgumentType);
    }

    private void createBuildResponseMethod(MethodMetadata businessMethod){
        List<LiteralMetadata> arguments = new ArrayList<>();

        LiteralMetadata targetLiteralUUID = getLiteralMetadata(LiteralType.VAR_NAME,"error","JsonApiError",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteral1 = getLiteralMetadata(LiteralType.VAR_NAME,"errorCode","String",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteral2 = getLiteralMetadata(LiteralType.VAR_NAME,"errorMessage","String",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteral3 = getLiteralMetadata(LiteralType.VAR_NAME,"id","String",AssignmentPosition.TARGET,null,null);
        arguments.add(targetLiteral3);
        arguments.add(targetLiteral1);
        arguments.add(targetLiteral2);
        businessMethod.addToBlockStatements(buildConstructorCallStatement(1,"JsonApiError",targetLiteralUUID,arguments));

        List<LiteralMetadata> argumentSetDetail = new ArrayList<>();
        LiteralMetadata targetLiteralDetail = getLiteralMetadata(LiteralType.VAR_NAME,"detail","String",AssignmentPosition.TARGET,null,null);
        argumentSetDetail.add(targetLiteralDetail);
        businessMethod.addToBlockStatements(buildMethodCallStatement(1,"error","setDetail", null,argumentSetDetail,null));

        LiteralMetadata paramLiteralError = getLiteralMetadata(LiteralType.ARGUMENT_NAME,"error", "String",null,null,null);
        LogicStatementMetadata param2Expression = buildConstructorCallStatement(1,"JsonApiError", paramLiteralError,null);

        LiteralMetadata param2CommandResponse =  new LiteralMetadata(LiteralType.ARGUMENT_AS_EXPRESSION) ;
        param2CommandResponse.setExpression(param2Expression);
        List<LiteralMetadata> argumentsCommandResponse = new ArrayList<>();
        argumentsCommandResponse.add(param2CommandResponse);

        LogicStatementMetadata innerExpression1 =  buildConstructorCallStatement(2,"JsonApiErrorDocument", null,argumentsCommandResponse);

        LiteralMetadata argumentOuter =  new LiteralMetadata(LiteralType.ARGUMENT_AS_EXPRESSION) ;
        argumentOuter.setExpression(innerExpression1);
        List<LiteralMetadata> argumentsOuter = new ArrayList<>();
        argumentsOuter.add(argumentOuter);

        LogicStatementMetadata bodyMethodCall = buildMethodCallStatement(1,"","body", null,argumentsOuter,null);
        LiteralMetadata paramLiteralStatus = getLiteralMetadata(LiteralType.ARGUMENT_NAME,"status", "String",null,null,null);
        List<LiteralMetadata> arguments1 = new ArrayList<>();
        arguments1.add(paramLiteralStatus);
        List<LogicStatementMetadata> chainedCallStatements = new ArrayList<>();
        chainedCallStatements.add(bodyMethodCall);
        businessMethod.addToBlockStatements(buildMethodCallStatement(1,"ResponseEntity","status", null,arguments1,chainedCallStatements));
    }

    private void buildGenerateResponseId(MethodMetadata businessMethod){
        LiteralMetadata targetLiteralResponse = getLiteralMetadata(LiteralType.ARGUMENT_AS_EXPRESSION,"UUID", "UUID",AssignmentPosition.TARGET,null,null);
        List<LogicStatementMetadata> chainedCallStatements = new ArrayList<>();
        LogicStatementMetadata innerMethodCallToString = buildMethodCallStatement(1,"UUID","toString", null,null,null);
        LiteralMetadata literalMetadataToString =  new LiteralMetadata(LiteralType.ARGUMENT_AS_EXPRESSION) ;
        literalMetadataToString.setExpression(innerMethodCallToString);
        chainedCallStatements.add(innerMethodCallToString);
        targetLiteralResponse.setExpression(buildMethodCallStatement(1,"UUID","randomUUID", null,null,chainedCallStatements));
        businessMethod.addToBlockStatements(buildReturnStatement(1,targetLiteralResponse));
    }

    private void buildMethodLogicMetadata(MethodMetadata businessMethod){
        LiteralMetadata targetLiteral = getLiteralMetadata(LiteralType.VAR_NAME,"status", "HttpStatus",AssignmentPosition.TARGET,null,null);
        LogicStatementMetadata assignmentStatement = buildMethodCallStatement(1,"HttpStatus","INTERNAL_SERVER_ERROR", targetLiteral,null,null);
        businessMethod.addToBlockStatements(assignmentStatement);

        LiteralMetadata targetLiteralErrorCode = getLiteralMetadata(LiteralType.VAR_NAME,"genericSystemExceptionCode", "this",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteralErrorDescription = getLiteralMetadata(LiteralType.VAR_NAME,"genericSystemExceptionCodeDesc", "this",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteralDetails = getLiteralMetadata(LiteralType.VAR_NAME,"genericSystemExceptionDetails", "this",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteralResponse = getLiteralMetadata(LiteralType.VAR_NAME,"response", "ResponseEntity<Object>",AssignmentPosition.TARGET,null,null);

        List<LiteralMetadata> arguments7  = new ArrayList<>();
        arguments7.add(targetLiteral);
        arguments7.add(targetLiteralErrorCode);
        arguments7.add(targetLiteralErrorDescription);
        arguments7.add(targetLiteralDetails);

        LogicStatementMetadata innerMethodCallMockitToAny = buildMethodCallStatement(1,"this","generateResponseId", null,null,null);

        LiteralMetadata literalMetadata = new LiteralMetadata(LiteralType.ARGUMENT_AS_EXPRESSION);
        literalMetadata.setExpression(innerMethodCallMockitToAny);
        arguments7.add(literalMetadata);
        businessMethod.addToBlockStatements(buildMethodCallStatement(1,"this","buildResponse", targetLiteralResponse,arguments7,null));
        businessMethod.addToBlockStatements(buildReturnStatement(1,targetLiteralResponse));
    }


    private void buildMethodLogicMetadataForHandlerNotFoundException(MethodMetadata businessMethod){
        LiteralMetadata targetLiteral = getLiteralMetadata(LiteralType.VAR_NAME,"status", "HttpStatus",AssignmentPosition.TARGET,null,null);
        LogicStatementMetadata assignmentStatement = buildMethodCallStatement(1,"HttpStatus","NOT_FOUND", targetLiteral,null,null);
        businessMethod.addToBlockStatements(assignmentStatement);

        LiteralMetadata targetLiteralErrorCode = getLiteralMetadata(LiteralType.VAR_NAME,"errorCode", "String",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteralResourceNotFoundExceptionCode = getLiteralMetadata(LiteralType.VAR_NAME,"resourceNotFoundExceptionCode", "this",AssignmentPosition.TARGET,null,null);

        LogicStatementMetadata assignmentStatementErrorCode = getLogicStatementMetadata(1,STATEMENTTYPE.ASSIGNMENT,targetLiteralErrorCode,targetLiteralResourceNotFoundExceptionCode);
        businessMethod.addToBlockStatements(assignmentStatementErrorCode);

        LiteralMetadata targetLiteralErrorDescription = getLiteralMetadata(LiteralType.VAR_NAME,"errorDescription", "String",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteralResourceNotFoundExceptionDesc = getLiteralMetadata(LiteralType.VAR_NAME,"resourceNotFoundExceptionDesc", "String",AssignmentPosition.SOURCE,null,OPERATORS.ASSIGNMENT);
        LogicStatementMetadata assignmentStatementResourceNotFoundExceptionDesc = getLogicStatementMetadata(1,STATEMENTTYPE.ASSIGNMENT,targetLiteralErrorDescription,targetLiteralResourceNotFoundExceptionDesc);
        businessMethod.addToBlockStatements(assignmentStatementResourceNotFoundExceptionDesc);

        LiteralMetadata targetLiteralDetails = getLiteralMetadata(LiteralType.VAR_NAME,"detail", "String",AssignmentPosition.TARGET,null,null);
        LiteralMetadata targetLiteralResourceNotFoundExceptionDetails = getLiteralMetadata(LiteralType.VAR_NAME,"resourceNotFoundExceptionDetails", "String",AssignmentPosition.SOURCE,null,OPERATORS.ASSIGNMENT);
        LogicStatementMetadata assignmentStatementResourceNotFoundExceptionDetails = getLogicStatementMetadata(1,STATEMENTTYPE.ASSIGNMENT,targetLiteralDetails,targetLiteralResourceNotFoundExceptionDetails);
        businessMethod.addToBlockStatements(assignmentStatementResourceNotFoundExceptionDetails);

        LiteralMetadata targetLiteralResponse = getLiteralMetadata(LiteralType.VAR_NAME,"response", "ResponseEntity<Object>",AssignmentPosition.TARGET,null,null);
        List<LiteralMetadata> arguments7  = new ArrayList<>();
        arguments7.add(targetLiteral);
        arguments7.add(targetLiteralErrorCode);
        arguments7.add(targetLiteralErrorDescription);
        arguments7.add(targetLiteralDetails);

        LogicStatementMetadata innerMethodCallMockitToAny = buildMethodCallStatement(1,"this","generateResponseId", null,null,null);

        LiteralMetadata literalMetadata = new LiteralMetadata(LiteralType.ARGUMENT_AS_EXPRESSION);
        literalMetadata.setExpression(innerMethodCallMockitToAny);
        arguments7.add(literalMetadata);
        businessMethod.addToBlockStatements(buildMethodCallStatement(1,"this","buildResponse", targetLiteralResponse,arguments7,null));
        businessMethod.addToBlockStatements(buildReturnStatement(1,targetLiteralResponse));
    }

    private LogicStatementMetadata getLogicStatementMetadata(int sequence, STATEMENTTYPE statementType, LiteralMetadata targetLiteralErrorCode, LiteralMetadata targetLiteralResourceNotFoundException) {
        LogicStatementMetadata assignmentStatementErrorCode = new LogicStatementMetadata(sequence);
        assignmentStatementErrorCode.setStatementType(statementType);
        assignmentStatementErrorCode.addToLiteralMetadata(targetLiteralErrorCode);
        assignmentStatementErrorCode.addToLiteralMetadata(targetLiteralResourceNotFoundException);
        return assignmentStatementErrorCode;
    }

    public ComponentTemplate getComponentTemplate(){
        return componentTemplate;
    }

    public String getPackageName() {
        return namingContext.getPackageName();
    }
}
