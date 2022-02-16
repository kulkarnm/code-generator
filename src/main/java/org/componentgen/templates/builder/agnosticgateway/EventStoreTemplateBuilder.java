package org.componentgen.templates.builder.agnosticgateway;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;

public class EventStoreTemplateBuilder extends AbstractTemplateBuilder {
    public EventStoreTemplateBuilder(ComponentTemplate componentTemplate, ComponentNamingContext namingContext){
        super(componentTemplate,namingContext);
    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage){
        List<String> packagesToImport = new ArrayList<>();
        packagesToImport.add(namingContext.getEventListenerClassName());

        ClassMetadata eventStore = new ClassMetadata(namingContext.getEventStoreClassName(),AccessModifier.PUBLIC,null,null,null,null,false,CLASSTYPE.INTERFACE);
        eventStore.setParentMetadata(parentPackage);
        return eventStore;
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass){
        return null;
    }

    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass){
        return null ;
    }

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass){
        List<MethodMetadata> methods = new ArrayList<>();

        MethodMetadata businessMethod = new MethodMetadata(namingContext.getMethodName(), AccessModifier.PUBLIC,null,namingContext.getEventStoreResponseClassName());
        businessMethod.setParentClassMetadata(parentClass);
        if(namingContext.getApiType() == APITYPE.COMMAND){
            ClassMetadata webAttributes = new ClassMetadata(namingContext.getEventStoreRequestClassName(),null,null,null,null,null,false,CLASSTYPE.CLASS);
            businessMethod.addToArguments(namingContext.getEventStoreRequestReferenceName(),webAttributes);
        }else {
            List<LiteralMetadata> arguments4 = getBusinessMethodArguments();
            for(LiteralMetadata arg : arguments4){
                businessMethod.addToArguments(arg.getVarName(),
                        new ClassMetadata(arg.getUserDefinedType(),null,null,null,null,null,false,CLASSTYPE.CLASS));
            }
        }
        methods.add(businessMethod);
        return methods ;
    }

    public String getPackageName() {
        return namingContext.getGatewayPackageName();
    }
}
