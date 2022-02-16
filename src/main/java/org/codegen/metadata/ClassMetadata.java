package org.codegen.metadata;

import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.CLASSTYPE;
import org.codegen.metadata.constants.OtherModifier;

import java.util.ArrayList;
import java.util.List;

public class ClassMetadata implements TypeMetadata{
    private String className;
    private List<String> packagesToImport;
    private AccessModifier accessModifier;
    private List<OtherModifier> otherModifiers;
    private String extendsFrom;
    private String extendsNarrow;
    private String narrow;
    private List<String> implementsFrom;
    private List<FieldMetadata> variables ;
    private List<MethodMetadata> methods ;
    private List<ConstructorMetadata> constructors ;
    private List<AnnotationMetadata> annotations;
    private PackageMetadata parentMetadata;
    private boolean isPrimitive;
    private CLASSTYPE classtype ;

    public ClassMetadata (String className, AccessModifier accessModifier,List<OtherModifier> otherModifiers,List<String> packagesToImport,
                          String extendsFrom, List<String> implementsFrom,boolean isPrimitive,CLASSTYPE classtype ){
        this.className = className;
        this.packagesToImport = packagesToImport;
        this.accessModifier = accessModifier;
        this.otherModifiers = otherModifiers;
        this.extendsFrom = extendsFrom;
        this.implementsFrom = implementsFrom;
        this.variables = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.annotations = new ArrayList<>();
        this.isPrimitive = isPrimitive;
        this.classtype = classtype;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getPackagesToImport() {
        return packagesToImport;
    }

    public void setPackagesToImport(List<String> packagesToImport) {
        this.packagesToImport = packagesToImport;
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
    }

    public List<OtherModifier> getOtherModifiers() {
        return otherModifiers;
    }

    public void setOtherModifiers(List<OtherModifier> otherModifiers) {
        this.otherModifiers = otherModifiers;
    }

    public String getExtendsFrom() {
        return extendsFrom;
    }

    public void setExtendsFrom(String extendsFrom) {
        this.extendsFrom = extendsFrom;
    }

    public String getExtendsNarrow() {
        return extendsNarrow;
    }

    public void setExtendsNarrow(String extendsNarrow) {
        this.extendsNarrow = extendsNarrow;
    }

    public String getNarrow() {
        return narrow;
    }

    public void setNarrow(String narrow) {
        this.narrow = narrow;
    }

    public List<String> getImplementsFrom() {
        return implementsFrom;
    }

    public void setImplementsFrom(List<String> implementsFrom) {
        this.implementsFrom = implementsFrom;
    }

    public List<FieldMetadata> getVariables() {
        return variables;
    }

    public void setVariables(List<FieldMetadata> variables) {
        this.variables = variables;
    }

    public List<MethodMetadata> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodMetadata> methods) {
        this.methods = methods;
    }

    public List<ConstructorMetadata> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<ConstructorMetadata> constructors) {
        this.constructors = constructors;
    }

    public List<AnnotationMetadata> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationMetadata> annotations) {
        this.annotations = annotations;
    }

    public PackageMetadata getParentMetadata() {
        return parentMetadata;
    }

    public void setParentMetadata(PackageMetadata parentMetadata) {
        this.parentMetadata = parentMetadata;
        this.parentMetadata.addToEnclosedClasses(this);
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }

    public CLASSTYPE getClasstype() {
        return classtype;
    }

    public void setClasstype(CLASSTYPE classtype) {
        this.classtype = classtype;
    }

    public void addToVariables(FieldMetadata field){
        this.variables.add(field);
    }

    public void addToMethods(MethodMetadata method){
        this.methods.add(method);
    }

    @Override
    public void addToBlockStatements(LogicStatementMetadata logicStatementMetadata) {
        //do nothing
    }

    public void addToConstructors(ConstructorMetadata constructor){
        this.constructors.add(constructor);
    }
    public void addToAnnotations(AnnotationMetadata annotation){
        this.annotations.add(annotation);
    }

}
