package org.codegen.metadata;


import java.util.ArrayList;
import java.util.List;

public class PackageMetadata implements TypeMetadata {
    private String packageName;
    private List<ClassMetadata> enclosedClasses ;

    public PackageMetadata(String packageName) {
        this.packageName = packageName;
        this.enclosedClasses = new ArrayList<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public List<ClassMetadata> getEnclosedClasses() {
        return enclosedClasses;
    }

    public void addToEnclosedClasses(ClassMetadata classMetadata){
        this.enclosedClasses.add(classMetadata);
    }
    @Override
    public void addToAnnotations(AnnotationMetadata annotationMetadata) {

    }

    @Override
    public void addToVariables(FieldMetadata fieldMetadata) {

    }

    @Override
    public void addToMethods(MethodMetadata methodMetadata) {

    }

    @Override
    public List<AnnotationMetadata> getAnnotations() {
        return null;
    }

    @Override
    public void addToBlockStatements(LogicStatementMetadata logicStatementMetadata) {

    }
}
