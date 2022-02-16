package org.codegen.metadata;

import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.LiteralType;
import org.codegen.metadata.constants.OtherModifier;
import org.codegen.metadata.constants.VariableType;

import java.util.ArrayList;
import java.util.List;

public class FieldMetadata implements TypeMetadata {
    private String name;
    private AccessModifier accessModifier;
    private List<OtherModifier> otherModifiers;
    private String compositeType;
    private TypeMetadata parentMetadata;
    private List<AnnotationMetadata> annotations;
    private String initializedValue ;
    private LogicStatementMetadata assignedExpression;

    public FieldMetadata(String name, AccessModifier accessModifier, List<OtherModifier> otherModifiers, String compositeType) {
        this.name = name;
        this.accessModifier = accessModifier;
        this.otherModifiers = otherModifiers;
        this.compositeType = compositeType;
        this.annotations = new ArrayList<>();
    }

    public FieldMetadata(String name, AccessModifier accessModifier, List<OtherModifier> otherModifiers, String compositeType, List<AnnotationMetadata> annotations) {
        this.name = name;
        this.accessModifier = accessModifier;
        this.otherModifiers = otherModifiers;
        this.compositeType = compositeType;
        this.annotations = annotations;
    }

    public LiteralMetadata obtainLiteralMetadata() {
        LiteralMetadata targetLiteral = new LiteralMetadata(LiteralType.VAR_REF);
        targetLiteral.setVariableType(VariableType.USER_DEFINED);
        targetLiteral.setVarName(name);
        targetLiteral.setUserDefinedType(compositeType);
        return targetLiteral;
    }

    public String getName() {
        return name;
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public List<OtherModifier> getOtherModifiers() {
        return otherModifiers;
    }

    public String getCompositeType() {
        return compositeType;
    }

    public TypeMetadata getParentMetadata() {
        return parentMetadata;
    }

    public List<AnnotationMetadata> getAnnotations() {
        return annotations;
    }

    public String getInitializedValue() {
        return initializedValue;
    }

    public LogicStatementMetadata getAssignedExpression() {
        return assignedExpression;
    }

    public void setParentMetadata(TypeMetadata parentMetadata) {
        this.parentMetadata = parentMetadata;
        this.parentMetadata.addToVariables(this);
    }

    public void setAssignedExpression(LogicStatementMetadata assignedExpression) {
        this.assignedExpression = assignedExpression;
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

    @Override
    public void addToBlockStatements(LogicStatementMetadata logicStatementMetadata) {

    }


}
