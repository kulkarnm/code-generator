package org.componentgen.templates.builder.agnosticgateway;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.config.AttributeConfig;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventResponseTemplateBuilder extends AbstractTemplateBuilder {
    public EventResponseTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        List<String> packagesToImportForWebRequest = new ArrayList<>();

        ClassMetadata webResponse = new ClassMetadata(namingContext.getEventStoreResponseClassName(), AccessModifier.PUBLIC, null, packagesToImportForWebRequest, null, null, false, CLASSTYPE.CLASS);
        webResponse.setParentMetadata(parentPackage);
        return webResponse;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        List<FieldMetadata> fields = new ArrayList<>();
        List<AttributeConfig> webResponseAttributes = namingContext.getWebResponse();
        webResponseAttributes.forEach(responseAtt-> {
            FieldMetadata field = new FieldMetadata(responseAtt.getAttributeName(),AccessModifier.PRIVATE,null,responseAtt.getAttributeType());
            field.setParentMetadata(parentClass);
            fields.add(field);
        });
        return fields;
    }

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass){
        List<MethodMetadata> methods = new ArrayList<>();
        List<AttributeConfig> webResponseAttributes = namingContext.getWebResponse();

        webResponseAttributes.forEach(responseAtt-> {

            MethodMetadata getMethod = buildGetterMethod(new FieldMetadata(responseAtt.getAttributeName(),AccessModifier.PUBLIC,null,responseAtt.getAttributeType()));
            getMethod.setParentClassMetadata(parentClass);
            methods.add(getMethod);

            MethodMetadata method = new MethodMetadata("set" + responseAtt.getAttributeType(),AccessModifier.PUBLIC,null,"void");
            method.setParentClassMetadata(parentClass);
            ClassMetadata argumentId = new ClassMetadata(responseAtt.getAttributeType(),null,null,null,null,null,false,CLASSTYPE.CLASS);
            method.addToArguments(responseAtt.getAttributeName(), argumentId);
            buildMethodLogicMetadataForSetterMethod(method,responseAtt.getAttributeName(),argumentId.getClassName());
            methods.add(method);
        });
        return methods ;
    }

    @Override
    public String getPackageName() { return namingContext.getGatewayPackageName();}

    public ComponentTemplate getComponentTemplate(){
        return componentTemplate;
    }
    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass){
        return null;
    }
}
