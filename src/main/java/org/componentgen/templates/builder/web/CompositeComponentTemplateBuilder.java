package org.componentgen.templates.builder.web;

import org.codegen.metadata.ComponentNamingContext;
import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.MethodArgumentMetadata;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractCompositeBuilder;

import java.util.List;

public class CompositeComponentTemplateBuilder extends AbstractCompositeBuilder {
    public static final String WEB = "Web" ;
    public static final String PARAM_TYPE_REQUEST = "Request" ;
    public static final String URI_PARAM_NAME_ID = "id" ;
    public static final String URI_CLASS_TYPE = "DetokenizedValue" ;

    public ComponentTemplate buildComponentTemplate(ComponentNamingContext componentNamingContext){
        ComponentTemplate componentTemplate = new ComponentTemplate();
        List<MethodArgumentMetadata> argms =getMethodArgumentMetadata(componentNamingContext,componentNamingContext.getEndpointRequestParams()) ;
        List<LiteralMetadata> identifiers = buildIdentifierArguments(argms,"DetokenizedValue" );

        RestControllerTemplateBuilder restControllerTemplateBuilder = new RestControllerTemplateBuilder(componentTemplate,componentNamingContext) ;
        restControllerTemplateBuilder.setBusinessMethodArguments(identifiers);
        componentTemplate = restControllerTemplateBuilder.buildTemplate(componentTemplate);

        WebResponseTemplateBuilder webResponseTemplateBuilder = new WebResponseTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = webResponseTemplateBuilder.buildTemplate(componentTemplate);

        WebResponseDataTemplateBuilder webResponseDataTemplateBuilder = new WebResponseDataTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = webResponseDataTemplateBuilder.buildTemplate(componentTemplate);

        WebResponseAttributesTemplateBuilder webResponseAttributesTemplateBuilder = new WebResponseAttributesTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = webResponseAttributesTemplateBuilder.buildTemplate(componentTemplate);

        WebRequestTemplateBuilder webRequestTemplateBuilder = new WebRequestTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = webRequestTemplateBuilder.buildTemplate(componentTemplate);

        WebRequestDataTemplateBuilder webRequestDataTemplateBuilder = new WebRequestDataTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = webRequestDataTemplateBuilder.buildTemplate(componentTemplate);

        WebRequestAttributesTemplateBuilder webRequestAttributesTemplateBuilder = new WebRequestAttributesTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = webRequestAttributesTemplateBuilder.buildTemplate(componentTemplate);

        ExceptionHandlerTemplateBuilder exceptionHandlerTemplateBuilder = new ExceptionHandlerTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = exceptionHandlerTemplateBuilder.buildTemplate(componentTemplate);
        return componentTemplate;
    }

}
