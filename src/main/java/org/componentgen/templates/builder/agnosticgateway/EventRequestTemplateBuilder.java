package org.componentgen.templates.builder.agnosticgateway;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.config.AttributeConfig;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRequestTemplateBuilder extends AbstractTemplateBuilder {
    public EventRequestTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            List<String> packagesToImportForWebRequest = new ArrayList<>();
            ClassMetadata webResponse = new ClassMetadata(namingContext.getEventStoreRequestClassName(), AccessModifier.PUBLIC, null, packagesToImportForWebRequest, null, null, false, CLASSTYPE.CLASS);
            webResponse.setExtendsNarrow(namingContext.getWebRequestDataClassName());
            webResponse.setParentMetadata(parentPackage);
            return webResponse;
        }
        return null;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata classMetadata) {
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            List<FieldMetadata> fields = new ArrayList<>();

            List<String> identifiers = namingContext.getIdentifierNames();
            for(String identifier: identifiers){
                FieldMetadata field = new FieldMetadata(identifier,AccessModifier.PRIVATE,null,"DetokenizedValue");
                field.setParentMetadata(classMetadata);
                fields.add(field);
            }
            Map<String,List<String>> requestParams = namingContext.getRequestParams();
            for(String requestParamKey : requestParams.keySet()){
                FieldMetadata field = new FieldMetadata(requestParamKey, AccessModifier.PRIVATE, null, "String");
                field.setParentMetadata(classMetadata);
                fields.add(field);
            }
            List<AttributeConfig> webRequestAttributes = namingContext.getWebRequest();
            webRequestAttributes.forEach(requestAtt->{
                FieldMetadata field = new FieldMetadata(requestAtt.getAttributeName(), AccessModifier.PRIVATE, null, requestAtt.getAttributeType());
                field.setParentMetadata(classMetadata);
                fields.add(field);
            });
            return fields;
        }
        return null;
    }

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata classMetadata) {
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            List<MethodMetadata> methods = new ArrayList<>();

            List<String> identifiers = namingContext.getIdentifierNames();
            for(String identifier: identifiers){
                MethodMetadata getMethod = buildGetterMethod(new FieldMetadata(identifier, AccessModifier.PUBLIC, null, "DetokenizedValue"));
                getMethod.setParentClassMetadata(classMetadata);
                methods.add(getMethod);

                MethodMetadata setMethod = new MethodMetadata("set" + identifier.substring(0,1).toUpperCase() + identifier.substring(1), AccessModifier.PUBLIC, null, "void");
                setMethod.setParentClassMetadata(classMetadata);

                ClassMetadata argumentId = new ClassMetadata("DetokenizedValue", null,null,null,null,null,false,CLASSTYPE.CLASS);
                setMethod.addToArguments(identifier,argumentId);
                buildMethodLogicMetadataForSetterMethod(setMethod,identifier,argumentId.getClassName());
                methods.add(setMethod);
            }

            Map<String,List<String>> requestParams = namingContext.getRequestParams();
            for(String identifier : requestParams.keySet()){
                MethodMetadata getMethod = buildGetterMethod(new FieldMetadata(identifier, AccessModifier.PUBLIC, null, "String"));
                getMethod.setParentClassMetadata(classMetadata);
                methods.add(getMethod);

                MethodMetadata setMethod = new MethodMetadata("set" + identifier.substring(0,1).toUpperCase() + identifier.substring(1), AccessModifier.PUBLIC, null, "void");
                setMethod.setParentClassMetadata(classMetadata);

                ClassMetadata argumentId = new ClassMetadata("String", null,null,null,null,null,false,CLASSTYPE.CLASS);
                setMethod.addToArguments(identifier,argumentId);
                buildMethodLogicMetadataForSetterMethod(setMethod,identifier,argumentId.getClassName());
                methods.add(setMethod);
            }

            List<AttributeConfig> webRequestAttributes = namingContext.getWebRequest();
            webRequestAttributes.forEach(requestAtt->{
                MethodMetadata getMethod = buildGetterMethod(new FieldMetadata(requestAtt.getAttributeName(), AccessModifier.PUBLIC, null, requestAtt.getAttributeType()));
                getMethod.setParentClassMetadata(classMetadata);
                methods.add(getMethod);

                MethodMetadata setMethod = new MethodMetadata("set" + requestAtt.getAttributeName().substring(0,1).toUpperCase() + requestAtt.getAttributeName().substring(1), AccessModifier.PUBLIC, null, "void");
                setMethod.setParentClassMetadata(classMetadata);

                ClassMetadata argumentId = new ClassMetadata(requestAtt.getAttributeType(), null,null,null,null,null,false,CLASSTYPE.CLASS);
                setMethod.addToArguments(requestAtt.getAttributeName(),argumentId);
                buildMethodLogicMetadataForSetterMethod(setMethod,requestAtt.getAttributeName(),argumentId.getClassName());
                methods.add(setMethod);
            });
            return methods;
        }
        return null;
    }

    @Override
    public String getPackageName() {
        return namingContext.getGatewayPackageName();
    }
    public ComponentTemplate getComponentTemplate(){
        return componentTemplate;
    }
    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass) {
        return null;
    }
}
