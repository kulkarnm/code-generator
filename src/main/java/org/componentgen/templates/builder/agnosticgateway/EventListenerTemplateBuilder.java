package org.componentgen.templates.builder.agnosticgateway;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;

import java.util.ArrayList;
import java.util.List;

public class EventListenerTemplateBuilder extends AbstractTemplateBuilder {
    public static final String VOID ="void";
    public EventListenerTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate, namingContext);
    }
    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        List<String> packagesToImportForEvent = new ArrayList<>();
        packagesToImportForEvent.add(namingContext.getEventListenerClassName());

        ClassMetadata eventListener = new ClassMetadata(namingContext.getEventListenerClassName(),
                AccessModifier.PUBLIC,null,packagesToImportForEvent,null,null,false, CLASSTYPE.INTERFACE);
        eventListener.setNarrow("E");
        eventListener.setParentMetadata(parentPackage);
        return eventListener;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass) { return null;}

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass){
        List<MethodMetadata> methods = new ArrayList<>();
        MethodMetadata handeEvent = new MethodMetadata("handleEvent",AccessModifier.PUBLIC,null,VOID);
        handeEvent.setParentClassMetadata(parentClass);
        ClassMetadata argument = new ClassMetadata("E",null,null,null,null,null,false,CLASSTYPE.CLASS);
        handeEvent.addToArguments("e",argument);
        methods.add(handeEvent);

        MethodMetadata logActivity = new MethodMetadata("logActivity",AccessModifier.PUBLIC,null,VOID);
        logActivity.setParentClassMetadata(parentClass);
        logActivity.addToArguments("e",argument);
        methods.add(logActivity);

        MethodMetadata logECrime = new MethodMetadata("logECrime",AccessModifier.PUBLIC,null,VOID);
        logECrime.setParentClassMetadata(parentClass);
        logECrime.addToArguments("e",argument);
        methods.add(logECrime);

        return methods;


    }
    @Override
    public String getPackageName() {
        return namingContext.getGatewayPackageName();
    }

}

