package org.codegen.generate;


import com.helger.jcodemodel.*;
import org.codegen.metadata.AnnotationMetadata;
import org.codegen.metadata.AnnotationParam;
import org.codegen.metadata.TypeMetadata;
import org.codegen.metadata.constants.AccessModifier;
import org.codegen.metadata.constants.OtherModifier;
import org.codegen.metadata.constants.VariableType;

import java.util.List;
import java.util.Map;

public abstract class SegmentGenerator <T extends TypeMetadata> {
    public static final String ANNOTATION_PARAM_VALUE = "NONE";
    private JCodeModel cm;

    public SegmentGenerator(JCodeModel cm){this.cm=cm;}

    protected static int resolveAccessModifier(int defaultMethod, AccessModifier accessModifier){
        int finalMods = defaultMethod;
        switch (accessModifier){
            case DEFAULT:
                finalMods = finalMods | JMod.NONE ;
                break;
            case PRIVATE:
                finalMods = finalMods | JMod.PRIVATE ;
                break;
            case PROTECTED :
                finalMods = finalMods | JMod.PROTECTED ;
                break;
            case PUBLIC :
                finalMods = finalMods | JMod.PUBLIC;
        }
        return finalMods;
    }

    protected static int resolveOtherModifier(int defaultMod, OtherModifier otherModifier){
        int finalMods = defaultMod;
        switch(otherModifier){
            case ABSTRACT :
                finalMods = finalMods | JMod.ABSTRACT ;
                break;
            case FINAL :
                finalMods = finalMods | JMod.FINAL ;
                break;
            case SYNCHRONIZED :
                finalMods = finalMods | JMod.SYNCHRONIZED ;
                break;
            case STATIC :
                finalMods = finalMods |JMod.STATIC ;
        }
        return finalMods;
    }

    public JCodeModel getCm(){
        return this.cm;
    }

    public void buildAnnotations(TypeMetadata typeMetadata, IJAnnotatable dc){
        List<AnnotationMetadata> annotations = typeMetadata.getAnnotations();
        if( null !=annotations){
            JAnnotationUse annotationUse = null;
            AbstractJClass destinationClass = null;
            for(AnnotationMetadata annotation : annotations){
                Map<String,List<AnnotationParam>> params = annotation.getParams();
                try {
                    destinationClass = cm.ref(annotation.getName());
                    annotationUse = dc.annotate(destinationClass);
                    if(null == destinationClass){
                        destinationClass=cm._class(annotation.getName());
                        annotationUse=dc.annotate(destinationClass);

                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                if(null != params ){
                    for(Map.Entry<String,List<AnnotationParam>> entry : params.entrySet()){
                        if(ANNOTATION_PARAM_VALUE.equals(entry.getKey())){
                            resolveParam(entry.getKey(),entry.getValue(),"value",annotationUse);
                        }else{
                            resolveParam(entry.getKey(),entry.getValue(),annotation.getName(),annotationUse);
                        }
                    }
                }
            }
        }
    }

    private void resolveParam(String annotationParamKey, List<AnnotationParam> annotationParamList,String annotationName, JAnnotationUse annotationUse){
        IJExpression[] class1 = new IJExpression[annotationParamList.size()];
        int i = 0;
        try{
            for(AnnotationParam annotationParam : annotationParamList){
                if(annotationParam.getType().equals(VariableType.ENUM)){
                    JDefinedClass enumClass = cm._class(annotationParam.getName(),EClassType.ENUM);
                    annotationUse.param(annotationParamKey,enumClass);
                }else if(annotationParam.getType().equals(VariableType.STRING) && ANNOTATION_PARAM_VALUE.equals(annotationParamKey)){
                    annotationUse.param(annotationName,annotationParam.getName());
                }else if(annotationParam.getType().equals(VariableType.STRING) && ! ANNOTATION_PARAM_VALUE.equals(annotationParamKey)) {
                    annotationUse.param(annotationParamKey,annotationParam.getName());
                }else if(annotationParam.getType().equals(VariableType.CLASS)) {
                    AbstractJClass destinationClass = cm.ref(annotationParam.getName());
                    if(null == destinationClass){
                        destinationClass = cm._class(annotationParam.getName());
                    }
                    class1[i] = destinationClass.dotclass();
                    i++;
                    if(i == annotationParamList.size()){
                        annotationUse.paramArray(annotationName,class1);
                    }
                }
            }
        }catch(JClassAlreadyExistsException ex){
            ex.printStackTrace();
        }
    }
    public abstract void generateSegment(T typeMetadata) throws JClassAlreadyExistsException ;
}
