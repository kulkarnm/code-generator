package org.codegen.generate;

import com.helger.jcodemodel.*;
import org.codegen.metadata.ArgumentMetadata;
import org.codegen.metadata.ClassMetadata;
import org.codegen.metadata.MethodMetadata;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.OtherModifier;

import java.util.List;

public class MethodGenerator extends SegmentGenerator<MethodMetadata>{
    private IJClassContainer jClassContainer;
    public MethodGenerator(IJClassContainer jClassContainer, JCodeModel cm) {
        super(cm);
        this.jClassContainer=jClassContainer;
    }

    public void generateSegment(MethodMetadata methodMetadata) throws JClassAlreadyExistsException {
        JDefinedClass jDefinedClass = (JDefinedClass) jClassContainer;
        AccessModifier accessModifier = methodMetadata.getAccessModifier();
        List<OtherModifier> otherModifiers = methodMetadata.getOtherModifiers();
        int finalMod = resolveAccessModifier(0, accessModifier);
        if (null != otherModifiers) {
            for (OtherModifier modifier : otherModifiers) {
                finalMod = resolveOtherModifier(finalMod, modifier);
            }
        }
        String returnType = methodMetadata.getReturnType();
        JMethod m = null;
        String name = methodMetadata.getMethodName();
        if (jDefinedClass.isInterface()) {
            m = jDefinedClass.method(JMod.PUBLIC, getCm().ref(returnType), name);
            m.body().virtual(true);
            m.body().directStatement(";");
        } else {
            m = jDefinedClass.method(finalMod, getCm().ref(returnType), name);
        }
        buildAnnotations(methodMetadata, m);
        List<ArgumentMetadata> methodArguments = methodMetadata.getArguments();
        for (ArgumentMetadata argument : methodArguments) {
            String argName = argument.getArgumentName();
            ClassMetadata type = argument.getArgumentType();
            AbstractJType argType = getCm().ref(type.getClassName());
            JVar parameter = m.param(argType, argName);
            buildAnnotations(type, parameter);
        }
       MethodBlockGenerator methodBlockGenerator = new MethodBlockGenerator(m, getCm());
        methodBlockGenerator.generateSegment(methodMetadata);
    }
}
