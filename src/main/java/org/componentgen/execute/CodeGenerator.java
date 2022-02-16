package org.componentgen.execute;

import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import org.codegen.generate.PackageGenerator;
import org.codegen.generate.SegmentGenerator;
import org.codegen.metadata.ComponentNamingContext;
import org.codegen.metadata.PackageMetadata;
import org.componentgen.templates.ComponentTemplate;
import org.componentgen.templates.builder.agnosticgateway.GatewayCompositeComponentTemplateBuilder;
import org.componentgen.templates.builder.service.ServiceCompositeComponentTemplateBuilder;
import org.componentgen.templates.builder.web.CompositeComponentTemplateBuilder;

import java.io.File;
import java.io.IOException;

public class CodeGenerator {

    public static void main(String[] args) throws IOException, JClassAlreadyExistsException {
        CodeGenerator codeGenerator = new CodeGenerator();
        ComponentNamingContext componentNamingContext = new ComponentNamingContext();
        componentNamingContext.buildNames();

        buildGatewayComponent(codeGenerator,componentNamingContext);
        buildServiceComponent(codeGenerator,componentNamingContext);
        buildWebComponent(codeGenerator,componentNamingContext);
    }

    private static void buildGatewayComponent(CodeGenerator codeGenerator,ComponentNamingContext componentNamingContext) throws IOException, JClassAlreadyExistsException {
        GatewayCompositeComponentTemplateBuilder gatewayCompositeComponentTemplateBuilder = new GatewayCompositeComponentTemplateBuilder();
        ComponentTemplate gatewayComponentTemplate =  gatewayCompositeComponentTemplateBuilder.buildComponentTemplate(componentNamingContext);
        SegmentGenerator packageGenerator0 = codeGenerator.generateCode(gatewayComponentTemplate);
        packageGenerator0.getCm().build(new File("//Users//kulkarnm//app//generated//gateway"));
    }

    private static void buildServiceComponent(CodeGenerator codeGenerator,ComponentNamingContext componentNamingContext) throws IOException, JClassAlreadyExistsException {
        ServiceCompositeComponentTemplateBuilder serviceCompositeComponentTemplateBuilder = new ServiceCompositeComponentTemplateBuilder();
        ComponentTemplate serviceComponentTemplate =  serviceCompositeComponentTemplateBuilder.buildComponentTemplate(componentNamingContext);
        SegmentGenerator packageGenerator1 = codeGenerator.generateCode(serviceComponentTemplate);
        packageGenerator1.getCm().build(new File("//Users//kulkarnm//app//generated//service"));

    /*    ServiceTestCompositeComponentTemplateBuilder serviceTestCompositeComponentTemplateBuilder = new ServiceTestCompositeComponentTemplateBuilder();
        ComponentTemplate serviceTestComponentTemplate =  serviceTestCompositeComponentTemplateBuilder.buildComponentTemplate(componentNamingContext);
        SegmentGenerator packageGenerator2 = codeGenerator.generateCode(serviceTestComponentTemplate);
        packageGenerator2.getCm().build(new File("c:\\generated\\service"));
    */
        }

    private static void buildWebComponent(CodeGenerator codeGenerator,ComponentNamingContext componentNamingContext) throws IOException, JClassAlreadyExistsException {
        CompositeComponentTemplateBuilder webCompositeComponentTemplateBuilder = new CompositeComponentTemplateBuilder();
        ComponentTemplate webComponentTemplate =  webCompositeComponentTemplateBuilder.buildComponentTemplate(componentNamingContext);
        SegmentGenerator packageGenerator3 = codeGenerator.generateCode(webComponentTemplate);
        packageGenerator3.getCm().build(new File("//Users//kulkarnm//app//generated//web"));

/*
        RestControllerTestCompositeComponentTemplateBuilder restControllerTestCompositeComponentTemplateBuilder = new RestControllerTestCompositeComponentTemplateBuilder();
        ComponentTemplate restControllerTestComponentTemplate =  restControllerTestCompositeComponentTemplateBuilder.buildComponentTemplate(componentNamingContext);
        SegmentGenerator packageGenerator4 = codeGenerator.generateCode(restControllerTestComponentTemplate);
        packageGenerator4.getCm().build(new File("c:\\generated\\web"));
*/

    }

    private SegmentGenerator generateCode(ComponentTemplate componentTemplate) throws JClassAlreadyExistsException {
        final JCodeModel cm = new JCodeModel();
        SegmentGenerator packageGenerator = new PackageGenerator(null,cm);
        PackageMetadata packageMetadata = componentTemplate.getPackageMetadata();
        packageGenerator.generateSegment(packageMetadata);
        return packageGenerator;
    }
}
