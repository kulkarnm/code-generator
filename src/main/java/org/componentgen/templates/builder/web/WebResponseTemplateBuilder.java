package org.componentgen.templates.builder.web;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;

public class WebResponseTemplateBuilder  extends AbstractTemplateBuilder {
    public WebResponseTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
            List<String> packagesToImportForWebRequest = new ArrayList<>();

            ClassMetadata webResponse = new ClassMetadata(namingContext.getWebResponseClassName(), AccessModifier.PUBLIC, null, packagesToImportForWebRequest, "JsonApiDataDocument", null, false, CLASSTYPE.CLASS);
            webResponse.setExtendsNarrow(namingContext.getWebResponseDataClassName());
            webResponse.setParentMetadata(parentPackage);
            return webResponse;
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
