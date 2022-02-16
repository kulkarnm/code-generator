package org.codegen.generate;

import com.helger.jcodemodel.*;
import org.codegen.metadata.ClassMetadata;
import org.codegen.metadata.ConstructorMetadata;
import org.codegen.metadata.FieldMetadata;
import org.codegen.metadata.MethodMetadata;
import org.codegen.metadata.constants.CLASSTYPE;

import java.util.List;

public class ClassGenerator extends SegmentGenerator<ClassMetadata>{
    private IJClassContainer jClassContainer;
    public ClassGenerator(IJClassContainer jClassContainer, JCodeModel cm) {
        super(cm);
        this.jClassContainer=jClassContainer;
    }
    @Override
    public void generateSegment(ClassMetadata classMetadata){
        if(classMetadata.getClasstype()== CLASSTYPE.INTERFACE){
            generateInterfaceSegment(classMetadata);
        }else if(classMetadata.getClasstype()==CLASSTYPE.ENUM){
        }else {
            generateClassSegment(classMetadata);
        }
    }

    private void generateClassSegment(ClassMetadata classMetadata){
        JDefinedClass dc = null;
        try{
            dc=jClassContainer.getPackage()._class(classMetadata.getClassName());
            buildClassExtends(classMetadata,dc);
            buildImplements(classMetadata,dc);
            buildAnnotations(classMetadata,dc);
            buildConstructors(classMetadata,dc);
            buildMethods(classMetadata,dc);
            buildFields(classMetadata,dc);
        }catch(JClassAlreadyExistsException ex){
            ex.printStackTrace();
        }
    }

    private void generateInterfaceSegment(ClassMetadata classMetadata){
        IJClassContainer dc = null;
        try{
            dc=jClassContainer.getPackage()._interface(classMetadata.getClassName());
            applyNarrow(classMetadata,dc);
            buildMethods(classMetadata,dc);
            buildFields(classMetadata,dc);

        }catch(JClassAlreadyExistsException ex){
            ex.printStackTrace();
        }
    }

    private void applyNarrow(ClassMetadata classMetadata, IJClassContainer dc){
        try{
            if(null != classMetadata.getNarrow()){
                AbstractJClass narrowedClass=this.getCm().ref(classMetadata.getNarrow());
                if(null != narrowedClass){
                    ((JDefinedClass)dc).narrow(narrowedClass);
                }else{
                    ((JDefinedClass)dc).narrow(this.getCm().directClass(classMetadata.getNarrow()));
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void buildClassExtends(ClassMetadata classMetadata,AbstractJClass dc){
        try {
            if (null != classMetadata.getExtendsFrom()) {
                AbstractJClass destinationClass = this.getCm().ref(classMetadata.getExtendsFrom());
                if (null == destinationClass) {
                    destinationClass = this.getCm()._class(classMetadata.getExtendsFrom());
                }

                AbstractJClass extendsNarrow = this.getCm().ref(classMetadata.getExtendsNarrow());
                if (null == destinationClass) {
                    destinationClass = this.getCm()._class(classMetadata.getExtendsNarrow());
                }

                if (null != classMetadata.getExtendsFrom()) {
                    if (classMetadata.getExtendsNarrow() == null) {
                        ((JDefinedClass) dc)._extends(destinationClass);
                    } else {
                        ((JDefinedClass) dc)._extends(destinationClass.narrow(extendsNarrow));
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void buildImplements(ClassMetadata classMetadata,AbstractJClass dc){
        try{
            List<String> implementsFrom = classMetadata.getImplementsFrom();
            if(null != implementsFrom ){
                for(String implementsClass : implementsFrom){
                    AbstractJClass implementsC = this.getCm().ref(implementsClass);
                    if(null == implementsC){
                        implementsC = this.getCm()._class(implementsClass);
                    }
                    ((JDefinedClass)dc)._implements(implementsC);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void buildConstructors(ClassMetadata classMetadata,IJClassContainer dc) throws JClassAlreadyExistsException{
        SegmentGenerator<ConstructorMetadata> constructorSegmentGenerator = new ConstructorGenerator(dc,getCm());
        List<ConstructorMetadata> constructors = classMetadata.getConstructors();
        if(null != constructors){
            for(ConstructorMetadata constructor : constructors){
                constructorSegmentGenerator.generateSegment(constructor);
            }
        }
    }

    private void buildMethods(ClassMetadata classMetadata, IJClassContainer dc) throws JClassAlreadyExistsException{
        SegmentGenerator<MethodMetadata> methodSegmentGenerator   = new MethodGenerator(dc,getCm());
        List<MethodMetadata> enclosedMethods = classMetadata.getMethods();
        if(null != enclosedMethods){
            for(MethodMetadata enclosedMethod : enclosedMethods){
                methodSegmentGenerator.generateSegment(enclosedMethod);
            }
        }
    }

    private void buildFields(ClassMetadata classMetadata, IJClassContainer dc) throws JClassAlreadyExistsException {
        SegmentGenerator<FieldMetadata> fieldSegmentGenerator = new FieldGenerator(dc,getCm());
        List<FieldMetadata> enclosedFields = classMetadata.getVariables();
        if(null != enclosedFields){
            for(FieldMetadata enclosedField : enclosedFields){
                fieldSegmentGenerator.generateSegment(enclosedField);
            }
        }
    }
}
