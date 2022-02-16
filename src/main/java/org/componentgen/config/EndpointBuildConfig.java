package org.componentgen.config;

import org.componentgen.templates.builder.constants.APITYPE;
import org.componentgen.templates.builder.constants.ENDPOINTOPERATION;

import java.util.ArrayList;
import java.util.List;

public class EndpointBuildConfig {
    private String apiName;
    private APITYPE apiType;
    private ENDPOINTOPERATION endpointType;
    private String endpointUri ;
    private String apiResponseTypeValue;
    private List<PathVariableConfig> pathVariableConfigs ;
    private List<AttributeConfig> webResponse ;
    private List<AttributeConfig> webRequest ;
    private List<RequestParamConfig> requestParamConfigs ;

    public EndpointBuildConfig(){
        this.webResponse = new ArrayList<>();
        this.webRequest = new ArrayList<>();
        this.pathVariableConfigs = new ArrayList<>();
        this.requestParamConfigs = new ArrayList<>();
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public APITYPE getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        if(apiType.equals("COMMAND")){
            this.apiType = APITYPE.COMMAND ;
        }else if(apiType.equals("QUERY")) {
            this.apiType = APITYPE.QUERY;
        }
    }

    public ENDPOINTOPERATION getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(String endpointType) {
        if( endpointType.equals("CREATE")){
            this.endpointType = ENDPOINTOPERATION.CREATE;
        }else if( endpointType.equals("UPDATE")){
            this.endpointType = ENDPOINTOPERATION.UPDATE;
        }else if(endpointType.equals("DELETE")){
            this.endpointType = ENDPOINTOPERATION.DELETE;
        }
    }

    public String getEndpointUri() {
        return endpointUri;
    }

    public void setEndpointUri(String endpointUri) {
        this.endpointUri = endpointUri;
    }

    public String getApiResponseTypeValue() {
        return apiResponseTypeValue;
    }

    public void setApiResponseTypeValue(String apiResponseTypeValue) {
        this.apiResponseTypeValue = apiResponseTypeValue;
    }

    public List<PathVariableConfig> getPathVariableConfigs() {
        return pathVariableConfigs;
    }

    public void setPathVariableConfigs(List<PathVariableConfig> pathVariableConfigs) {
        this.pathVariableConfigs = pathVariableConfigs;
    }

    public List<AttributeConfig> getWebResponse() {
        return webResponse;
    }

    public void setWebResponse(List<AttributeConfig> webResponse) {
        this.webResponse = webResponse;
    }

    public List<AttributeConfig> getWebRequest() {
        return webRequest;
    }

    public void setWebRequest(List<AttributeConfig> webRequest) {
        this.webRequest = webRequest;
    }

    public List<RequestParamConfig> getRequestParamConfigs() {
        return requestParamConfigs;
    }

    public void setRequestParamConfigs(List<RequestParamConfig> requestParamConfigs) {
        this.requestParamConfigs = requestParamConfigs;
    }

    public void addWebRequestAttributeConfig(AttributeConfig attributeConfig){
        webRequest.add(attributeConfig);
    }
    public void addWebResponseAttributeConfig(AttributeConfig attributeConfig){
        webResponse.add(attributeConfig);
    }
    public void addPathVariableConfigs(PathVariableConfig pathVariableConfig){
        pathVariableConfigs.add(pathVariableConfig);
    }

    public void addRequestParamConfig(RequestParamConfig requestParamConfig){
        this.requestParamConfigs.add(requestParamConfig);
    }


}
