package org.codegen.metadata;

import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.OtherModifier;

import java.nio.file.attribute.AclEntry;
import java.util.ArrayList;
import java.util.List;

public class ConstructorMetadata implements TypeMetadata{
    private AccessModifier accessModifier;
    private List<OtherModifier> otherModifiers;
    private String methodName;
    private String returnType;
    private List<ArgumentMetadata> arguments ;
    private List<AnnotationMetadata> annotations;
    private ClassMetadata parentClassMetadata;
    private List<LogicStatementMetadata> blockStatements ;
    private FieldMetadata returnValueToBeAssignedTo;

    public ConstructorMetadata(String methodName,AccessModifier accessModifier, List<OtherModifier> otherModifiers,  String returnType) {
        this.accessModifier = accessModifier;
        this.otherModifiers = otherModifiers;
        this.methodName = methodName;
        this.returnType = returnType;
        this.arguments = new ArrayList<>();
        this.annotations = new ArrayList<>();
        this.blockStatements = new ArrayList<>();
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public List<OtherModifier> getOtherModifiers() {
        return otherModifiers;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<ArgumentMetadata> getArguments() {
        return arguments;
    }

    public List<AnnotationMetadata> getAnnotations() {
        return annotations;
    }

    public ClassMetadata getParentClassMetadata() {
        return parentClassMetadata;
    }

    public List<LogicStatementMetadata> getBlockStatements() {
        return blockStatements;
    }

    public FieldMetadata getReturnValueToBeAssignedTo() {
        return returnValueToBeAssignedTo;
    }

    public void setParentClassMetadata(ClassMetadata parentClassMetadata) {
        this.parentClassMetadata = parentClassMetadata;
        this.parentClassMetadata.addToConstructors(this);
    }

    public void addToAnnotations(AnnotationMetadata annotation){
        this.annotations.add(annotation);
    }

    @Override
    public void addToVariables(FieldMetadata fieldMetadata) {

    }

    @Override
    public void addToMethods(MethodMetadata methodMetadata) {

    }

    public void addToArguments(String name,ClassMetadata classMetadata){
        this.arguments.add(new ArgumentMetadata(name, classMetadata));
    }
    public void addToBlockStatements(LogicStatementMetadata statementMetadata){
        this.blockStatements.add(statementMetadata);
    }

    public void setAccessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
    }

    public void setOtherModifiers(List<OtherModifier> otherModifiers) {
        this.otherModifiers = otherModifiers;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setAnnotations(List<AnnotationMetadata> annotations) {
        this.annotations = annotations;
    }

}
