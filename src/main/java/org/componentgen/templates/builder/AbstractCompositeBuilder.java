package org.componentgen.templates.builder;

import org.codegen.metadata.ComponentNamingContext;
import org.codegen.metadata.LiteralMetadata;
import org.codegen.metadata.MethodArgumentMetadata;
import org.codegen.metadata.constants.LiteralType;
import org.componentgen.execute.EndpointRequestParamConfig;
import org.componentgen.templates.builder.constants.APITYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractCompositeBuilder {

    public List<LiteralMetadata> buildIdentifierArguments(List<MethodArgumentMetadata> argms,String argType){
        List<LiteralMetadata> arguments4 = new ArrayList<>();
        for(MethodArgumentMetadata arg : argms){
            LiteralMetadata argMD = new LiteralMetadata(LiteralType.ARGUMENT_NAME) ;
            argMD.setVarName(arg.getArgName());
            argMD.setUserDefinedType(arg.getArgType());
            argMD.setPathVariable(arg.isPathVariable());
            arguments4.add(argMD);
        }
        return arguments4;
    }
     private List<MethodArgumentMetadata> findArgumentsByType(List<MethodArgumentMetadata> argms,String argType){
        return argms.stream().filter(bma->bma.getArgType().equals(argType)).collect(Collectors.toList());
     }

     protected  List<MethodArgumentMetadata> getMethodArgumentMetadata(ComponentNamingContext componentNamingContext,List<EndpointRequestParamConfig> arguments){
        List<MethodArgumentMetadata> argms = new ArrayList<>();
        List<String> identifiers = componentNamingContext.getIdentifierNames();
        for(String identifier: identifiers){
            MethodArgumentMetadata identifierArgument = new MethodArgumentMetadata(identifier,"DetokenizedValue",true);
            argms.add(identifierArgument);
        }

         Map<String,List<String>> requestParams = componentNamingContext.getRequestParams();
        for(String requestParamKey : requestParams.keySet()){
            MethodArgumentMetadata requestParamArgument = new MethodArgumentMetadata(requestParamKey,"String",false);
            argms.add(requestParamArgument);
        }

        if(arguments == null && APITYPE.COMMAND.equals(componentNamingContext.getApiType())) {
            MethodArgumentMetadata requestParam2 = new MethodArgumentMetadata(componentNamingContext.getWebRequestReferenceName(),componentNamingContext.getWebRequestClassName(),false);
            argms.add(requestParam2);
        }else{
            for(EndpointRequestParamConfig arg : arguments) {
                argms.add(new MethodArgumentMetadata(arg.getArgumentName(),arg.getArgumentType(),arg.isPathVariable())) ;
            }
        }
        return argms;
     }
}
