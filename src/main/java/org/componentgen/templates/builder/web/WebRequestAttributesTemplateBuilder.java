package org.componentgen.templates.builder.web;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.config.AttributeConfig;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;

public class WebRequestAttributesTemplateBuilder extends AbstractTemplateBuilder {
    public WebRequestAttributesTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            List<String> packagesToImportForWebRequest = new ArrayList<>();
            packagesToImportForWebRequest.add(namingContext.getServicePackageName());
            packagesToImportForWebRequest.add(ComponentNamingContext.VALIDATION_PACKAGE);
            ClassMetadata webRequest = new ClassMetadata(namingContext.getWebRequestAttributesClassName(), AccessModifier.PUBLIC, null, packagesToImportForWebRequest, null, null, false, CLASSTYPE.CLASS);
            webRequest.setParentMetadata(parentPackage);
            return webRequest;
        }
        return null;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        List<FieldMetadata> fields = new ArrayList<>();
        List<AttributeConfig> webRequestAttributes = namingContext.getWebRequest();
        webRequestAttributes.forEach(requestAtt-> {
                FieldMetadata field = new FieldMetadata(requestAtt.getAttributeName(),AccessModifier.PRIVATE,null,requestAtt.getAttributeType());
                field.setParentMetadata(parentClass);
                fields.add(field);
            });
        return fields;
    }


    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass){
        List<MethodMetadata> methods = new ArrayList<>();
        List<AttributeConfig> webRequestAttributes = namingContext.getWebRequest();

        webRequestAttributes.forEach(requestAtt-> {

            MethodMetadata getMethod = buildGetterMethod(new FieldMetadata(requestAtt.getAttributeName(),AccessModifier.PUBLIC,null,requestAtt.getAttributeType()));
            getMethod.setParentClassMetadata(parentClass);
            methods.add(getMethod);

            MethodMetadata method = new MethodMetadata("set" + requestAtt.getAttributeType(),AccessModifier.PUBLIC,null,"void");
            method.setParentClassMetadata(parentClass);
            ClassMetadata argumentId = new ClassMetadata(requestAtt.getAttributeType(),null,null,null,null,null,false,CLASSTYPE.CLASS);
            method.addToArguments(requestAtt.getAttributeName(), argumentId);
            buildMethodLogicMetadataForSetterMethod(method,requestAtt.getAttributeName(),argumentId.getClassName());
            methods.add(method);
        });
        return methods ;
    }

    @Override
    public String getPackageName() {return namingContext.getPackageName();}

    public ComponentTemplate getComponentTemplate(){
        return componentTemplate;
    }
    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass){
        return null;
    }
}
