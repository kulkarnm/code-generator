package org.codegen.metadata;

import java.util.List;
import java.util.Map;

public class AnnotationMetadata implements TypeMetadata{
    private String name;
    private Map<String, List<AnnotationParam>> params;
    private TypeMetadata parentMetadata ;

    public AnnotationMetadata(String name, Map<String, List<AnnotationParam>> params) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public Map<String, List<AnnotationParam>> getParams() {
        return params;
    }

    public TypeMetadata getParentMetadata() {
        return parentMetadata;
    }

    public void setParentMetadata(TypeMetadata parentMetadata){
        this.parentMetadata = parentMetadata;
        this.parentMetadata.addToAnnotations(this);
    }

    public void addToAnnotations(AnnotationMetadata annotationMetadata){

    }
    public void addToVariables(FieldMetadata fieldMetadata){

    }

    public void addToMethods (MethodMetadata methodMetadata){

    }

    @Override
    public List<AnnotationMetadata> getAnnotations() {
        return null;
    }

    public void addToBlockStatements(LogicStatementMetadata logicStatementMetadata){

    }
}
