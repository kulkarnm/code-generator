package org.componentgen.templates.builder.web;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.*;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;
import org.componentgen.templates.builder.constants.ENDPOINTOPERATION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestControllerTemplateBuilder extends AbstractTemplateBuilder {
    public RestControllerTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext componentNamingContext){
        super(componentTemplate,componentNamingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage) {
        List<String> packagesToImportFromController = new ArrayList<>();
        packagesToImportFromController.add(ComponentNamingContext.COMMON_MASKUTILS);
        packagesToImportFromController.add(namingContext.getServicePackageName());
        packagesToImportFromController.add(namingContext.getServiceResponsePackageName());
        packagesToImportFromController.add(ComponentNamingContext.DETOKENIZED_VALUE);
        packagesToImportFromController.add(ComponentNamingContext.VALIDATION_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.LOGGER_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.LOGGER_FACTORY_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.HTTP_STATUS_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.HTTP_PATH_VARIABLE_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.HTTP_POST_MAPPING_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.HTTP_REQUEST_BODY_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.HTTP_RESPONSE_STATUS_PACKAGE);
        packagesToImportFromController.add(ComponentNamingContext.HTTP_RESTCONTROLLER_PACKAGE);
        AnnotationMetadata controllerAnnotation = new AnnotationMetadata("RestController",null);
        List<AnnotationMetadata> annotations = new ArrayList<>();
        annotations.add(controllerAnnotation);
        return buildClassMetadata(namingContext.getControllerClassName(),packagesToImportFromController,annotations,parentPackage, CLASSTYPE.CLASS,null);

    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        List<FieldMetadata> fields = new ArrayList<>();
        FieldMetadata commonDebitCommandService = new FieldMetadata(namingContext.getServiceReferenceName(), AccessModifier.PRIVATE,null,namingContext.getServiceClassName());
        commonDebitCommandService.setParentMetadata(parentClass);
        fields.add(commonDebitCommandService);
        fields.add(buildClassLevelLoggerFactory(parentClass,this.componentTemplate));
        return fields;
    }


    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass) {
        ConstructorMetadata constructor = new ConstructorMetadata(parentClass.getClassName(),AccessModifier.PUBLIC,null,"NONE");
        constructor.setParentClassMetadata(parentClass);
        ClassMetadata service = new ClassMetadata(namingContext.getServiceClassName(), null,null,null,null,null,false,CLASSTYPE.CLASS);
        constructor.addToArguments(namingContext.getServiceReferenceName(),service);
        buildConstructorLogicMetadata(constructor);
        return constructor;
    }

    private void buildConstructorLogicMetadata(ConstructorMetadata constructorMetadata){
        FieldMetadata instanceVariable = constructorMetadata.getParentClassMetadata().getVariables().stream().filter(fm ->fm.getName().equals(namingContext.getServiceReferenceName())).findFirst().get();
        LiteralMetadata targetLiteral1 = instanceVariable.obtainLiteralMetadata();
        targetLiteral1.setPosition(AssignmentPosition.TARGET);
        targetLiteral1.setOperator(OPERATORS.ASSIGNMENT);

        ArgumentMetadata argument = constructorMetadata.getArguments().stream().filter(fm ->fm.getArgumentName().equals(namingContext.getServiceReferenceName())).findFirst().get();
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
        List<MethodMetadata> methods = new ArrayList<>();
        MethodMetadata businessMethod = new MethodMetadata(namingContext.getMethodName(),AccessModifier.PUBLIC,null,namingContext.getWebResponseClassName()) ;
        businessMethod.setParentClassMetadata(parentClass);
        buildBusinessMethodAnnotations(businessMethod);
        buildBusinessMethodArguments(businessMethod);
        buildMethodLogicMetadata(businessMethod);
        methods.add(businessMethod);
         return methods ;
    }

    private void buildBusinessMethodAnnotations(MethodMetadata businessMethod){
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            AnnotationMetadata PostMapping = getAnnotationMetadata(namingContext.getUrl(),VariableType.STRING,"NONE", "PostMapping");
            PostMapping.setParentMetadata(businessMethod);

            AnnotationMetadata detokenize = new AnnotationMetadata("Detokenize",new HashMap<>());
            detokenize.setParentMetadata(businessMethod);

            if(namingContext.getEndpointOperation() == ENDPOINTOPERATION.CREATE) {
                AnnotationMetadata responseStatus = getAnnotationMetadata("HttpStatus.CREATED",VariableType.STRING,"code", "ResponseStatus");
                responseStatus.setParentMetadata(businessMethod);
            }else if(namingContext.getEndpointOperation() == ENDPOINTOPERATION.DELETE){
                AnnotationMetadata responseStatus = getAnnotationMetadata("HttpStatus.OK",VariableType.STRING,"code", "ResponseStatus");;
                responseStatus.setParentMetadata(businessMethod);
            }else if(namingContext.getEndpointOperation() == ENDPOINTOPERATION.UPDATE){
                AnnotationMetadata responseStatus = getAnnotationMetadata("HttpStatus.OK",VariableType.STRING,"code", "ResponseStatus");
                responseStatus.setParentMetadata(businessMethod);
            }

        }else {
            String urlWithoutRequestParam = namingContext.getUrl();
            urlWithoutRequestParam = urlWithoutRequestParam.substring(0,urlWithoutRequestParam.indexOf('?'));
            AnnotationMetadata getMapping = getAnnotationMetadata(urlWithoutRequestParam,VariableType.STRING,"NONE", "GetMapping");
            getMapping.setParentMetadata(businessMethod);

            AnnotationMetadata responseBody = new AnnotationMetadata("ResponseBody",null);
            responseBody.setParentMetadata(businessMethod);

            AnnotationMetadata detokenize = new AnnotationMetadata("Detokenize",new HashMap<>());
            detokenize.setParentMetadata(businessMethod);
        }
    }

    private AnnotationMetadata getAnnotationMetadata(String annotationParamName,VariableType annotationParamType,String annotationKey,String annotationName){
        Map<String,List<AnnotationParam>> propertySource = new HashMap<>();
        List<AnnotationParam> annotationParamList = new ArrayList<>();
        AnnotationParam annotationParam = new AnnotationParam(annotationParamName, annotationParamType);
        annotationParamList.add(annotationParam);
        propertySource.put(annotationKey,annotationParamList);
        return new AnnotationMetadata(annotationName,propertySource);
    }

    private void buildBusinessMethodArguments(MethodMetadata businessMethod){
        List<OtherModifier> otherModifiers = new ArrayList<>();
        otherModifiers.add(OtherModifier.FINAL);

        for(LiteralMetadata arg: this.businessMethodArguments) {
            ClassMetadata methodArgumentType = new ClassMetadata(arg.getUserDefinedType(), null, otherModifiers, null, null, null, false, CLASSTYPE.CLASS);
            if(arg.isPathVariable() && arg.getUserDefinedType().equals("DetokenizedValue")){
                AnnotationMetadata detokenizedPolicy = getAnnotationMetadata("TokenizationPolicy.NO_TOKENIZATION",VariableType.STRING,"policy", "DetokenizePolicy");
                detokenizedPolicy.setParentMetadata(methodArgumentType);

                AnnotationMetadata pathVariable = new AnnotationMetadata("PathVariable",null);
                pathVariable.setParentMetadata(methodArgumentType);
            }else {
                AnnotationMetadata requestParam = new AnnotationMetadata("requestParam",null);
                requestParam.setParentMetadata(methodArgumentType);
            }

            businessMethod.addToArguments(arg.getVarName(),methodArgumentType);
            if(namingContext.getApiType()== APITYPE.COMMAND && arg.getUserDefinedType().equalsIgnoreCase(namingContext.getWebRequestClassName())) {
                ClassMetadata webRequest = new ClassMetadata(arg.getUserDefinedType(),null,null,null,null,null,false,CLASSTYPE.CLASS);
                AnnotationMetadata Valid = new AnnotationMetadata("Valid",null);
                Valid.setParentMetadata(webRequest);
                AnnotationMetadata RequestBody = new AnnotationMetadata("RequestBody",null);
                RequestBody.setParentMetadata(webRequest);
                businessMethod.addToArguments(arg.getVarName(),webRequest);
            }
        }
    }

    private void buildMethodLogicMetadata(MethodMetadata businessMethod){
        buildLogDebugStatement(businessMethod);
        List<LiteralMetadata> arguments4 = getBusinessMethodArguments();

        if(namingContext.getApiType() == APITYPE.COMMAND) {
            LiteralMetadata targetLiteral1 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral1.setVarName("request");
            targetLiteral1.setUserDefinedType(namingContext.getEventStoreRequestClassName());
            targetLiteral1.setPosition(AssignmentPosition.TARGET);
            businessMethod.addToBlockStatements(buildConstructorCallStatement(1,namingContext.getEventStoreRequestClassName(),targetLiteral1,null));

            LiteralMetadata targetLiteral4 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral4.setVarName(namingContext.getServiceResponseReferenceName());
            targetLiteral4.setUserDefinedType(namingContext.getServiceResponseClassName());
            targetLiteral4.setPosition(AssignmentPosition.TARGET);

            List<LiteralMetadata> arguments2  = new ArrayList<>();
            arguments2.add(targetLiteral1);
            businessMethod.addToBlockStatements(buildMethodCallStatement(3,namingContext.getServiceReferenceName(),namingContext.getMethodName(), targetLiteral4,arguments2,null));

            LiteralMetadata targetLiteral5 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral5.setVarName("id");
            targetLiteral5.setUserDefinedType("String");
            targetLiteral5.setPosition(AssignmentPosition.TARGET);
            businessMethod.addToBlockStatements(buildMethodCallStatement(4,namingContext.getServiceResponseReferenceName(),"getId", targetLiteral5,null,null));

            LiteralMetadata targetLiteral7 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral7.setVarName(namingContext.getWebResponseReferenceName());
            targetLiteral7.setUserDefinedType(namingContext.getWebResponseClassName());
            targetLiteral7.setPosition(AssignmentPosition.TARGET);
            businessMethod.addToBlockStatements(buildConstructorCallStatement(7,namingContext.getWebResponseClassName(),targetLiteral7,null));
            businessMethod.addToBlockStatements(buildReturnStatement(9,targetLiteral7));
        }else {
            LiteralMetadata targetLiteral4 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral4.setVarName(namingContext.getServiceResponseReferenceName());
            targetLiteral4.setUserDefinedType(namingContext.getServiceResponseClassName());
            targetLiteral4.setPosition(AssignmentPosition.TARGET);

            businessMethod.addToBlockStatements(buildMethodCallStatement(4,namingContext.getServiceReferenceName(),namingContext.getMethodName(), targetLiteral4,arguments4,null));

            LiteralMetadata targetLiteral5 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral5.setVarName("id");
            targetLiteral5.setUserDefinedType("String");
            targetLiteral5.setPosition(AssignmentPosition.TARGET);
            businessMethod.addToBlockStatements(buildMethodCallStatement(5,namingContext.getServiceResponseReferenceName(),"getId", targetLiteral5,null,null));

            LiteralMetadata targetLiteral7 = new LiteralMetadata(LiteralType.VAR_NAME);
            targetLiteral7.setVarName(namingContext.getWebResponseReferenceName());
            targetLiteral7.setUserDefinedType(namingContext.getWebResponseClassName());
            targetLiteral7.setPosition(AssignmentPosition.TARGET);

            List<LiteralMetadata> arguments7 = new ArrayList<>();
            arguments7.add(targetLiteral5);
            arguments7.add(targetLiteral4);

            businessMethod.addToBlockStatements(buildConstructorCallStatement(7,namingContext.getWebResponseClassName(),targetLiteral7,null));
            businessMethod.addToBlockStatements(buildReturnStatement(9,targetLiteral7));
        }
    }

    private LiteralMetadata buildRequestAttributesRetrievalStatement(MethodMetadata businessMethod){
        LiteralMetadata targetLiteral3 = new LiteralMetadata(LiteralType.VAR_NAME);
        targetLiteral3.setVarName("attributes");
        targetLiteral3.setUserDefinedType(namingContext.getWebRequestAttributesClassName());
        targetLiteral3.setPosition(AssignmentPosition.TARGET);
        businessMethod.addToBlockStatements(buildMethodCallStatement(3,namingContext.getWebRequestReferenceName(),"getAttributes", targetLiteral3,null,null));
        return targetLiteral3;
    }
    public ComponentTemplate getComponentTemplate(){
        return componentTemplate;
    }

    public String getPackageName() {
        return namingContext.getPackageName();
    }
}
