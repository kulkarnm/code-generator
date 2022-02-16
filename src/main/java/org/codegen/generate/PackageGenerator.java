package org.codegen.generate;

import com.helger.jcodemodel.IJClassContainer;
import com.helger.jcodemodel.JClassAlreadyExistsException;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JPackage;
import org.codegen.metadata.ClassMetadata;
import org.codegen.metadata.PackageMetadata;

import java.util.List;

public class PackageGenerator extends SegmentGenerator<PackageMetadata> {
    private IJClassContainer jClassContainer;
     public PackageGenerator(IJClassContainer jClassContainer, JCodeModel cm){
         super(cm);
         this.jClassContainer = jClassContainer;
     }

     public void generateSegment(PackageMetadata packageMetadata) throws JClassAlreadyExistsException {
         if( null != packageMetadata.getPackageName() && !packageMetadata.getPackageName().equals("")) {
             JPackage jp = getCm()._package(packageMetadata.getPackageName());
             SegmentGenerator<ClassMetadata> classGenerator = new ClassGenerator(jp,getCm());
             List<ClassMetadata> enclosedClasses =  packageMetadata.getEnclosedClasses();
             for(ClassMetadata classMetadata : enclosedClasses){
                 classGenerator.generateSegment(classMetadata);
             }
         }
     }
}
