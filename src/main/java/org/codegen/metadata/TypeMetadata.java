package org.codegen.metadata;

import java.util.List;

public interface TypeMetadata {
    public void addToAnnotations(AnnotationMetadata annotationMetadata);
    public void addToVariables(FieldMetadata fieldMetadata);
    public void addToMethods (MethodMetadata methodMetadata);
    public List<AnnotationMetadata> getAnnotations();
    public void addToBlockStatements(LogicStatementMetadata logicStatementMetadata);
}
