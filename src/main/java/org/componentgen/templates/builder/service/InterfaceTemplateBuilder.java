package org.componentgen.templates.builder.service;

import org.codegen.metadata.*;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.AbstractTemplateBuilder;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;

public class InterfaceTemplateBuilder extends AbstractTemplateBuilder {
    public InterfaceTemplateBuilder(ComponentTemplate componentTemplate,
                                    ComponentNamingContext namingContext) {
        super(componentTemplate, namingContext);

    }

    @Override
    protected ClassMetadata buildClassMetadata(PackageMetadata parentPackage) {
        List<String> packagesToImportForController = new ArrayList<>();
        List<AnnotationMetadata> annotations = new ArrayList<>();
        return buildClassMetadata(namingContext.getServiceClassName(), packagesToImportForController,
                annotations,
                parentPackage, CLASSTYPE.INTERFACE, null);
    }

    @Override
    protected List<FieldMetadata> buildFieldsMetadata(ClassMetadata parentClass) {
        return null;
    }


    @Override
    protected ConstructorMetadata buildConstructorMetadata(ClassMetadata parentClass) {
        return null;
    }

    @Override
    protected List<MethodMetadata> buildMethodMetadata(ClassMetadata parentClass) {
        List<MethodMetadata> methods = new ArrayList<>();
        MethodMetadata businessMethod = new MethodMetadata(namingContext.getMethodName(),
                AccessModifier.PUBLIC, null,
                namingContext.getServiceResponseClassName());
        businessMethod.setParentClassMetadata(parentClass);

        if (namingContext.getApiType() == APITYPE.COMMAND) {

            ClassMetadata webAttributes = new ClassMetadata(
                    namingContext.getEventStoreRequestClassName(), null, null, null, null,
            null,false,CLASSTYPE.CLASS);
            businessMethod.addToArguments(namingContext.getEventStoreRequestReferenceName(), webAttributes);
        } else {
            List<LiteralMetadata> arguments4 = getBusinessMethodArguments();
            for (LiteralMetadata arg : arguments4) {
                businessMethod.addToArguments(arg.getVarName(),
                        new ClassMetadata(arg.getUserDefinedType(), null, null, null, null, null,
                                false, CLASSTYPE.CLASS));
            }

        }
        methods.add(businessMethod);
        return methods;
    }

    public ComponentTemplate getComponentTemplate() {
        return componentTemplate;
    }

    public String getPackageName() {
        return namingContext.getServicePackageName();
    }
}
