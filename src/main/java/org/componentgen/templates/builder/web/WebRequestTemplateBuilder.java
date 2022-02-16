package org.componentgen.templates.builder.web;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;

public class WebRequestTemplateBuilder extends AbstractTemplateBuilder {
    public WebRequestTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        if(namingContext.getApiType() == APITYPE.COMMAND) {
            List<String> packagesToImportForWebRequest = new ArrayList<>();
            packagesToImportForWebRequest.add(namingContext.getServicePackageName());
            packagesToImportForWebRequest.add(ComponentNamingContext.VALIDATION_PACKAGE);
            ClassMetadata webRequest = new ClassMetadata(namingContext.getWebRequestClassName(), AccessModifier.PUBLIC, null, packagesToImportForWebRequest, "JsonApiDataDocument", null, false, CLASSTYPE.CLASS);
            webRequest.setExtendsNarrow(namingContext.getWebRequestDataClassName());
            webRequest.setParentMetadata(parentPackage);
            return webRequest;
        }
        return null;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata classMetadata) {
        return null;
    }

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata classMetadata) {
        return null;
    }

    @Override
    public String getPackageName() {
        return namingContext.getPackageName();
    }
    public ComponentTemplate getComponentTemplate(){
        return componentTemplate;
    }

}
