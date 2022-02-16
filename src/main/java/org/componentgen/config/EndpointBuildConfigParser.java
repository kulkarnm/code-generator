package org.componentgen.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class EndpointBuildConfigParser {
    public static void main(String [] args){
        parse();
    }

    public static EndpointBuildConfig parse(){
        EndpointBuildConfig endpointBuildConfig = new EndpointBuildConfig();
        try{
            final InputStream input = EndpointBuildConfigParser.class.getClassLoader().getResourceAsStream("request-attributes-config-create.json");
            String json = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining("\n"));
            try{
                JsonFactory factory = new JsonFactory();
                ObjectMapper mapper = new ObjectMapper(factory);
                JsonNode rootNode = mapper.readTree(json);
                if( rootNode.has("apiName")) {
                    endpointBuildConfig.setApiName(rootNode.get("apiName").asText());
                }
                if(rootNode.has("apiType")){
                    endpointBuildConfig.setApiType(rootNode.get("apiType").asText());
                }
                if(rootNode.has("endpointType")){
                    endpointBuildConfig.setEndpointType(rootNode.get("endpointType").asText());
                }
                if(rootNode.has("endpointUri")){
                    endpointBuildConfig.setEndpointUri(rootNode.get("endpointUri").asText());
                }
                if(rootNode.has("apiResponseTypeValue")) {
                    endpointBuildConfig.setApiResponseTypeValue(rootNode.get("apiResponseTypeValue").asText());
                }
                if(rootNode.has("pathVariables")) {
                    Iterator<JsonNode> childNodes = rootNode.get("pathVariables").elements();
                    while(childNodes.hasNext()) {
                        PathVariableConfig pathVariableConfig = fillPathVariables(childNodes.next());
                        endpointBuildConfig.addPathVariableConfigs(pathVariableConfig);
                    }
                }
                if(rootNode.has("requestParams")) {
                    Iterator<JsonNode> childNodes = rootNode.get("requestParams").elements();
                    while(childNodes.hasNext()){
                        RequestParamConfig requestParamConfig = fillRequestParam(childNodes.next());
                        endpointBuildConfig.addRequestParamConfig(requestParamConfig);
                    }
                }
                if(rootNode.has("webResponse")) {
                    Iterator<JsonNode> childNodes = rootNode.get("webResponse").elements();
                    while(childNodes.hasNext()){
                        JsonNode attribute = childNodes.next();
                        Iterator<JsonNode> attrChildNodes = attribute.get("children").elements();
                        while(attrChildNodes.hasNext()){
                            AttributeConfig attributeConfig = fillRequestAttributes(attrChildNodes.next());
                            endpointBuildConfig.addWebResponseAttributeConfig(attributeConfig);
                        }
                    }
                }

                if(rootNode.has("webRequest")) {
                    Iterator<JsonNode> childNodes = rootNode.get("webRequest").elements();
                    while(childNodes.hasNext()){
                        JsonNode attribute = childNodes.next();
                        Iterator<JsonNode> attrChildNodes = attribute.get("children").elements();
                        while(attrChildNodes.hasNext()){
                            AttributeConfig attributeConfig = fillRequestAttributes(attrChildNodes.next());
                            endpointBuildConfig.addWebRequestAttributeConfig(attributeConfig);
                        }
                    }
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return endpointBuildConfig;
    }

    private static PathVariableConfig fillPathVariables(JsonNode node){
        PathVariableConfig pathVariableConfig = new PathVariableConfig();
        Iterator<Map.Entry<String,JsonNode>> elements = node.fields();
        while(elements.hasNext()){
            Map.Entry<String,JsonNode> elementField = elements.next();
            pathVariableConfig.set(elementField.getKey(),elementField.getValue().asText());
        }
        return pathVariableConfig;
    }


    private static RequestParamConfig fillRequestParam(JsonNode node){
        RequestParamConfig requestParamConfig = new RequestParamConfig();
        Iterator<Map.Entry<String,JsonNode>> elements = node.fields();
        while(elements.hasNext()){
            Map.Entry<String,JsonNode> elementField = elements.next();
            requestParamConfig.set(elementField.getKey(),elementField.getValue().asText());
        }
        return requestParamConfig;
    }

    private static AttributeConfig fillRequestAttributes(JsonNode node){
        AttributeConfig attributeConfig = new AttributeConfig();
        JsonNode children = node.get("children");
        if(children instanceof ArrayNode && node.size() >0){
            Iterator<Map.Entry<String,JsonNode>> elements = ((ObjectNode)node).fields() ;
            while (elements.hasNext()){
                Map.Entry<String,JsonNode> elementField = elements.next();
                attributeConfig.set(elementField.getKey(),elementField.getValue().asText());
            }
            Iterator<JsonNode> childElementIterator = ((ArrayNode)children).elements();
            while(childElementIterator.hasNext()){
                attributeConfig.addToChildren(fillRequestAttributes(childElementIterator.next()));
            }
        }else if(node.isValueNode()){

        }else {
            Iterator<Map.Entry<String,JsonNode>> elements = ((ObjectNode)node).fields() ;
            while (elements.hasNext()){
                Map.Entry<String,JsonNode> elementField = elements.next();
                attributeConfig.set(elementField.getKey(),elementField.getValue().asText());
            }
        }
        return attributeConfig ;
    }
}
