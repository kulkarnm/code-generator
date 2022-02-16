package org.componentgen.templates.builder.service;

import org.codegen.metadata.ComponentNamingContext;
import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.MethodArgumentMetadata;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractCompositeBuilder;

import java.util.List;

public class ServiceCompositeComponentTemplateBuilder extends AbstractCompositeBuilder {
    public static final String SERVICE_INTERFACE_REQUEST_NAME_ID = "id" ;
    public static final String SERVICE_INTERFACE_REQUEST_TYPE_ID = "String";

    public ComponentTemplate buildComponentTemplate(ComponentNamingContext componentNamingContext){
        ComponentTemplate componentTemplate = new ComponentTemplate();
        List<MethodArgumentMetadata> argms = getMethodArgumentMetadata(componentNamingContext,componentNamingContext.getEndpointRequestParams());
        List<LiteralMetadata> identifiers = buildIdentifierArguments(argms, "DetokenizedValue");
        ServiceImplTemplateBuilder serviceImplTemplateBuilder = new ServiceImplTemplateBuilder(componentTemplate,componentNamingContext);
        serviceImplTemplateBuilder.setBusinessMethodArguments(identifiers);

        componentTemplate = serviceImplTemplateBuilder.buildTemplate(componentTemplate);
        InterfaceTemplateBuilder interfaceTemplateBuilder = new InterfaceTemplateBuilder(componentTemplate,componentNamingContext);
        interfaceTemplateBuilder.setBusinessMethodArguments(identifiers);
        componentTemplate = interfaceTemplateBuilder.buildTemplate(componentTemplate);
        return componentTemplate;
    }
}
