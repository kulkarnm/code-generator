package org.codegen.generate;

import com.helger.jcodemodel.*;
import org.codegen.metadata.ArgumentMetadata;
import org.codegen.metadata.ClassMetadata;
import org.codegen.metadata.ConstructorMetadata;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.OtherModifier;

import java.util.List;

public class ConstructorGenerator extends SegmentGenerator<ConstructorMetadata>{
    private IJClassContainer jClassContainer;
    public ConstructorGenerator(IJClassContainer jClassContainer, JCodeModel cm) {
        super(cm);
        this.jClassContainer=jClassContainer;
    }

    public void generateSegment(ConstructorMetadata constructorMetadata) throws JClassAlreadyExistsException {
        JDefinedClass jDefinedClass = (JDefinedClass)jClassContainer;
        AccessModifier accessModifier = constructorMetadata.getAccessModifier();
        List<OtherModifier> otherModifiers = constructorMetadata.getOtherModifiers();
        int finalMod = resolveAccessModifier(0,accessModifier);
        if( null != otherModifiers){
            for(OtherModifier modifier:otherModifiers){
                finalMod=resolveOtherModifier(finalMod,modifier);
            }
        }
        JMethod m = null;
        m=jDefinedClass.constructor(JMod.PUBLIC);
        List<ArgumentMetadata> methodArguments = constructorMetadata.getArguments();
        for(ArgumentMetadata argument: methodArguments){
            String argName = argument.getArgumentName();
            ClassMetadata type = argument.getArgumentType();
            AbstractJType argType = getCm().ref(type.getClassName());
            JVar parameter = m.param(argType,argName);
            buildAnnotations(type,parameter);
        }
        ConstructorBlockGenerator constructorBlockGenerator = new ConstructorBlockGenerator(m, getCm());
        constructorBlockGenerator.generateSegment(constructorMetadata);
    }
}
