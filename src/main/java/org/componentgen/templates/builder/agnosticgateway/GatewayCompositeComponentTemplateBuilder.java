package org.componentgen.templates.builder.agnosticgateway;

import org.codegen.metadata.ComponentNamingContext;
import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.MethodArgumentMetadata;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractCompositeBuilder;

import java.util.List;

public class GatewayCompositeComponentTemplateBuilder extends AbstractCompositeBuilder {

    public ComponentTemplate buildComponentTemplate(ComponentNamingContext componentNamingContext){
        ComponentTemplate componentTemplate = new ComponentTemplate();
        List<MethodArgumentMetadata> argms =getMethodArgumentMetadata(componentNamingContext,componentNamingContext.getEndpointRequestParams()) ;
        List<LiteralMetadata> identifiers = buildIdentifierArguments(argms,"DetokenizedValue" );

        CommandTemplateBuilder commandTemplateBuilder = new CommandTemplateBuilder(componentTemplate,componentNamingContext) ;
        commandTemplateBuilder.setBusinessMethodArguments(identifiers);
        componentTemplate = commandTemplateBuilder.buildTemplate(componentTemplate);

        EventStoreTemplateBuilder eventStoreTemplateBuilder = new EventStoreTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = eventStoreTemplateBuilder.buildTemplate(componentTemplate);

        EventStoreImplTemplateBuilder eventStoreImplTemplateBuilder = new EventStoreImplTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = eventStoreImplTemplateBuilder.buildTemplate(componentTemplate);

        EventTemplateBuilder eventTemplateBuilder = new EventTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = eventTemplateBuilder.buildTemplate(componentTemplate);

        EventResponseTemplateBuilder eventResponseTemplateBuilder = new EventResponseTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = eventResponseTemplateBuilder.buildTemplate(componentTemplate);

        EventRequestTemplateBuilder evetRequestTemplateBuilder = new EventRequestTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = evetRequestTemplateBuilder.buildTemplate(componentTemplate);

        EventListenerTemplateBuilder eventListenerTemplateBuilder = new EventListenerTemplateBuilder(componentTemplate,componentNamingContext);
        componentTemplate = eventListenerTemplateBuilder.buildTemplate(componentTemplate);

        return componentTemplate;
    }
}
