package org.codegen.metadata;

import org.componentgen.config.*;
import org.componentgen.execute.EndpointRequestParamConfig;
import org.componentgen.templates.builder.constants.APITYPE;
import org.componentgen.templates.builder.constants.COMPONENTTYPE;
import org.componentgen.templates.builder.constants.ENDPOINTOPERATION;
import org.componentgen.templates.builder.constants.NAMETYPE;
import org.springframework.web.util.UriTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentNamingContext {
    public static final String COMMON_PACKAGE_PREFIX = "com.barclaycard.digital.";
    public static final String VALIDATION_PACKAGE = "javax.validation.Valid";
    public static final String LOGGER_PACKAGE = "org.slf4j.Logger";
    public static final String LOGGER_FACTORY_PACKAGE = "org.slf4j.LoggerFactory";
    public static final String COMMON_MASKUTILS = "com.barclaycard.digital.common.utilities.MaskUtils";
    public static final String DETOKENIZED_VALUE = "com.barclaycard.digital.web.tokenizer.DetokenizedValue";
    public static final String HTTP_STATUS_PACKAGE = "org.springframework.http.HttpStatus";
    public static final String HTTP_PATH_VARIABLE_PACKAGE = "org.springframework.web.bind.annotation.PathVariable";
    public static final String HTTP_POST_MAPPING_PACKAGE = "org.springframework.web.bind.annotation.PostMapping";

    public static final String HTTP_REQUEST_BODY_PACKAGE = "org.springframework.web.bind.annotation.RequestBody";
    public static final String HTTP_RESPONSE_STATUS_PACKAGE = "org.springframework.web.bind.annotation.ResponseStatus";
    public static final String HTTP_RESTCONTROLLER_PACKAGE = "org.springframework.web.bind.annotation.RestController";

    private String packageName;
    private String controllerClassName;

    private String webRequestClassName;
    private String webRequestReferenceName;
    private String webRequestDataClassName;
    private String webRequestAttributesClassName;
    private String webRequestAttributesReferenceName;
    private List<AttributeConfig> webRequest;

    private String webResponseClassName;
    private String webResponseReferenceName;
    private String webResponseDataClassName;
    private String webResponseAttributesClassName;
    private String webResponseAttributesReferenceName;
    private List<AttributeConfig> webResponse;

    private String methodName;
    private String servicePackageName;
    private String serviceResponsePackageName;
    private String serviceClassName;
    private String serviceReferenceName;
    private String serviceResponseClassName;
    private String serviceImplClassName;
    private String serviceResponseReferenceName;
    private String serviceResponseAttributesClassName;
    private String serviceResponseAttributesReferenceName;


    private String apiName;
    private String url;
    private List<String> identifierNames;
    private Map<String, List<String>> requestParams;
    private APITYPE apiType;
    private ENDPOINTOPERATION endpointOperation;

    private String eventClassName;
    private String eventReferenceName;
    private String eventListenerClassName;
    private String eventStoreClassName;
    private String eventStoreImplClassName;
    private String eventStoreReferenceName;
    private String eventStoreRequestClassName;
    private String eventStoreRequestReferenceName;
    private String eventStoreResponseClassName;
    private String commandClassName;
    private String gatewayPackageName;
    private String commandClassReferenceName;
    private String commandResponseClassName;
    private String commandResponseClassReferenceName;
    private List<EndpointRequestParamConfig> endpointRequestParams;
    private String apiResponseTypeValue;
    private String exceptionHandlerName;

    public ComponentNamingContext() {
        EndpointBuildConfig endpointBuildConfig = EndpointBuildConfigParser.parse();
        this.apiName = endpointBuildConfig.getApiName();
        this.apiType = endpointBuildConfig.getApiType();
        this.endpointOperation = endpointBuildConfig.getEndpointType();
        this.url = endpointBuildConfig.getEndpointUri();
        this.identifierNames = parsePathVariables();
        this.requestParams = parseRequestParams();
        this.apiResponseTypeValue = endpointBuildConfig.getApiResponseTypeValue();
        this.webRequest = endpointBuildConfig.getWebRequest();
        this.webResponse = endpointBuildConfig.getWebResponse();
        endpointRequestParams = new ArrayList<>();

        List<PathVariableConfig> pathVariables = endpointBuildConfig.getPathVariableConfigs();
        List<RequestParamConfig> requestParams = endpointBuildConfig.getRequestParamConfigs();
        if (null != pathVariables && !pathVariables.isEmpty()) {
            for (PathVariableConfig pathVariable : pathVariables) {
                EndpointRequestParamConfig endpointRequestParam = new EndpointRequestParamConfig(pathVariable.getPathVariableName(), pathVariable.getPathVariableValue(), true);
                endpointRequestParams.add(endpointRequestParam);
            }
        }

        if (null != requestParams && !requestParams.isEmpty()) {
            for (RequestParamConfig requestParam : requestParams) {
                EndpointRequestParamConfig endpointRequestParam = new EndpointRequestParamConfig(requestParam.getRequestParamName(), requestParam.getRequestParamValue(), false);
                endpointRequestParams.add(endpointRequestParam);
            }
        }
    }

    private List<String> parsePathVariables() {
        UriTemplate template = new UriTemplate(url);
        Map<String, String> matchTemplate = new HashMap<>();
        matchTemplate = template.match(url);
        Set<String> it = matchTemplate.keySet();
        List<String> list = it.stream().collect(Collectors.toList());
        return list;
    }

    public Map<String, List<String>> parseRequestParams() {
        try {
            URL urlPattern = new URL("http://localhost" + url);
            final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
            String[] pairs = null;
            if (null != urlPattern.getQuery()) {
                pairs = urlPattern.getQuery().split("&");
                for (String pair : pairs) {
                    final int idx = pair.indexOf("=");
                    final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                    if (!query_pairs.containsKey(key)) {
                        query_pairs.put(key, new LinkedList<String>());
                    }
                    final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                    query_pairs.get(key).add(value);
                }
                return query_pairs;
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void buildNames() {
        this.packageName = COMMON_PACKAGE_PREFIX + apiName.toLowerCase() + ".web";
        this.controllerClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.CONTROLLER);
        this.servicePackageName = COMMON_PACKAGE_PREFIX + buildCamelCaseStringForApiType(apiName, apiType, null, NAMETYPE.PACKAGE, COMPONENTTYPE.SERVICE_PACKAGE);
        this.serviceResponsePackageName = COMMON_PACKAGE_PREFIX + buildCamelCaseStringForApiType(apiName, apiType, null, NAMETYPE.PACKAGE, COMPONENTTYPE.SERVICE_PACKAGE)
                + buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.SERVICE_RESPONSE);
        this.serviceClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.SERVICE_INTERFACE);
        this.serviceImplClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.SERVICE_IMPL);
        this.serviceReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.SERVICE_INTERFACE);
        this.methodName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.METHOD, COMPONENTTYPE.METHOD);
        this.serviceResponseClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.WEB_RESPONSE_ATTRIBUTES);
        this.serviceResponseReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.WEB_RESPONSE_ATTRIBUTES);
        this.webRequestAttributesClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.WEB_REQUEST_ATTRIBUTES);
        this.serviceResponseAttributesClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.WEB_REQUEST_ATTRIBUTES);
        this.serviceResponseAttributesReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.WEB_REQUEST_ATTRIBUTES);

        this.webResponseClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.WEB_RESPONSE);
        this.webResponseReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.WEB_RESPONSE);
        this.webResponseDataClassName = buildCamelCaseStringForApiType(apiName, apiType, null, NAMETYPE.CLASS, COMPONENTTYPE.WEB_RESPONSE_DATA);
        this.webRequestReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.WEB_REQUEST);
        this.webRequestClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.WEB_REQUEST);
        this.webRequestDataClassName = buildCamelCaseStringForApiType(apiName, apiType, null, NAMETYPE.CLASS, COMPONENTTYPE.WEB_REQUEST_DATA);
        this.webRequestAttributesReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.WEB_REQUEST_ATTRIBUTES);
        this.webResponseAttributesClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.WEB_RESPONSE_ATTRIBUTES);
        this.webResponseAttributesReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.WEB_RESPONSE_ATTRIBUTES);

        this.eventClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.EVENT);
        this.eventReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.EVENT);
        this.eventListenerClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.EVENT_LISTENER);
        this.commandClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.COMMAND);
        this.commandClassReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.EVENT);
        this.commandResponseClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.COMMAND_RESPONSE);
        this.commandResponseClassReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.COMMAND_RESPONSE);
        this.gatewayPackageName = COMMON_PACKAGE_PREFIX + buildCamelCaseStringForApiType(apiName, apiType, null, NAMETYPE.PACKAGE, COMPONENTTYPE.GATEWAY_PACKAGE);
        this.eventStoreClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.EVENT_STORE);
        this.eventStoreImplClassName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.EVENT_STORE_IMPL);
        this.eventStoreReferenceName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.VARIABLE, COMPONENTTYPE.EVENT_STORE);
        this.eventStoreRequestClassName = buildCamelCaseStringForApiType(apiName, apiType, null, NAMETYPE.CLASS, COMPONENTTYPE.EVENT_STORE_REQUEST);
        this.eventStoreRequestReferenceName = eventStoreRequestClassName.substring(0,1).toLowerCase() + eventStoreRequestClassName.substring(1);
        this.eventStoreResponseClassName = buildCamelCaseStringForApiType(apiName, apiType, null, NAMETYPE.CLASS, COMPONENTTYPE.EVENT_STORE_RESPONSE);
        this.apiResponseTypeValue = "\"" + apiResponseTypeValue + "\"" ;
        this.exceptionHandlerName = buildCamelCaseStringForApiType(apiName, apiType, endpointOperation, NAMETYPE.CLASS, COMPONENTTYPE.EXCEPTION_HANDLER);
    }
     private String buildCamelCaseStringForApiType(String name,APITYPE apiType, ENDPOINTOPERATION endpointOperation,NAMETYPE nameType,COMPONENTTYPE componentType){
        String apiNameWithType = null;
        if(nameType == NAMETYPE.PACKAGE){
            apiNameWithType= name.toLowerCase();
        }else if(nameType == NAMETYPE.METHOD){
            apiNameWithType = name.substring(0,1).toLowerCase() + name.substring(1);
        }else if(nameType == NAMETYPE.VARIABLE && apiType == APITYPE.QUERY){
            name = name.toLowerCase();
            apiNameWithType = name.substring(0,1).toLowerCase() + name.substring(1);
        }else if(nameType == NAMETYPE.CLASS){
            apiNameWithType = name;
        }else {
            name = name.toLowerCase();
            apiNameWithType = name.substring(0,1).toUpperCase() + name.substring(1);
        }

        if(apiType == APITYPE.QUERY){
            if(nameType ==NAMETYPE.CLASS){
                apiNameWithType=apiNameWithType + "Query" ;
            }else if(nameType == NAMETYPE.METHOD){
                apiNameWithType = "get" + apiNameWithType.substring(0,1).toUpperCase() + apiNameWithType.substring(1);
            }
        }else {
            if(endpointOperation == ENDPOINTOPERATION.CREATE){
                if(nameType == NAMETYPE.CLASS){
                    apiNameWithType = apiNameWithType + "Command" ;
                    apiNameWithType = "Create" + apiNameWithType.substring(0,1).toUpperCase() + apiNameWithType.substring(1);
                }else if(nameType == NAMETYPE.METHOD || nameType == NAMETYPE.VARIABLE){
                    apiNameWithType = "create" + apiNameWithType.substring(0,1).toUpperCase() + apiNameWithType.substring(1);
                }
            } else if(endpointOperation == ENDPOINTOPERATION.UPDATE) {
                if(nameType == NAMETYPE.CLASS){
                    apiNameWithType = apiNameWithType + "Command" ;
                    apiNameWithType = "Update" + apiNameWithType.substring(0,1).toUpperCase() + apiNameWithType.substring(1);
                }else if(nameType == NAMETYPE.METHOD || nameType == NAMETYPE.VARIABLE){
                    apiNameWithType = "update" + apiNameWithType.substring(0,1).toUpperCase() + apiNameWithType.substring(1);
                }
            } else if(endpointOperation == ENDPOINTOPERATION.DELETE) {
                if(nameType == NAMETYPE.CLASS){
                    apiNameWithType = apiNameWithType + "Command" ;
                    apiNameWithType = "Delete" + apiNameWithType.substring(0,1).toUpperCase() + apiNameWithType.substring(1);
                }else if(nameType == NAMETYPE.METHOD || nameType == NAMETYPE.VARIABLE){
                    apiNameWithType = "delete" + apiNameWithType.substring(0,1).toUpperCase() + apiNameWithType.substring(1);
                }
            }
        }

        if(componentType ==COMPONENTTYPE.CONTROLLER){
            apiNameWithType=apiNameWithType + "Controller";
        }else if(componentType ==COMPONENTTYPE.WEB_REQUEST){
            apiNameWithType=apiNameWithType + "WebRequest";
        }else if(componentType ==COMPONENTTYPE.WEB_RESPONSE){
            apiNameWithType=apiNameWithType + "WebResponse";
        }else if(componentType ==COMPONENTTYPE.SERVICE_REQUEST){
            apiNameWithType=apiNameWithType + "ServiceRequest";
        }else if(componentType ==COMPONENTTYPE.SERVICE_RESPONSE){
            apiNameWithType=apiNameWithType + "ServiceResponse";
        }else if(componentType ==COMPONENTTYPE.SERVICE_INTERFACE){
            apiNameWithType=apiNameWithType + "Service";
        }else if(componentType ==COMPONENTTYPE.SERVICE_IMPL){
            apiNameWithType=apiNameWithType + "ServiceImpl";
        }else if(componentType ==COMPONENTTYPE.WEB_PACKAGE){
            apiNameWithType=apiNameWithType + ".web";
        }else if(componentType ==COMPONENTTYPE.SERVICE_PACKAGE){
            apiNameWithType=apiNameWithType + ".service";
        }else if(componentType ==COMPONENTTYPE.WEB_REQUEST_DATA){
            apiNameWithType=apiNameWithType + "RequestDate";
        }else if(componentType ==COMPONENTTYPE.WEB_REQUEST_ATTRIBUTES){
            apiNameWithType=apiNameWithType + "RequestAttributes";
        }else if(componentType == COMPONENTTYPE.SERVICE_RESPONSE_ATTRIBUTES){
            apiNameWithType=apiNameWithType + "ResponseAttributes";
        }else if(componentType ==COMPONENTTYPE.WEB_RESPONSE_DATA){
            apiNameWithType=apiNameWithType + "ResponseData";
        }else if(componentType ==COMPONENTTYPE.WEB_RESPONSE_ATTRIBUTES){
            apiNameWithType=apiNameWithType + "ResponseAttributes";
        }else if(componentType ==COMPONENTTYPE.COMMAND_RESPONSE){
            apiNameWithType=apiNameWithType + "Response";
        }else if(componentType ==COMPONENTTYPE.EVENT){
            apiNameWithType=apiNameWithType + "Event";
        }else if(componentType ==COMPONENTTYPE.EVENT_LISTENER){
            apiNameWithType=apiNameWithType + "EventListener";
        }else if(componentType ==COMPONENTTYPE.EVENT_STORE){
            apiNameWithType=apiNameWithType + "EventStore";
        }else if(componentType ==COMPONENTTYPE.EVENT_STORE_IMPL){
            apiNameWithType=apiNameWithType + "EventStoreImpl";
        }else if(componentType ==COMPONENTTYPE.COMMAND){
            apiNameWithType=apiNameWithType + "Command";
        }else if(componentType ==COMPONENTTYPE.GATEWAY_PACKAGE){
            apiNameWithType=apiNameWithType + ".eventstore";
        }else if(componentType ==COMPONENTTYPE.EXCEPTION_HANDLER){
            apiNameWithType=apiNameWithType + "ExceptionHandler";
        }else if(componentType ==COMPONENTTYPE.EVENT_STORE_REQUEST){
            apiNameWithType=apiNameWithType + "EventRequest";
        }else if(componentType ==COMPONENTTYPE.EVENT_STORE_RESPONSE){
            apiNameWithType=apiNameWithType + "EventResponse";
        }
        return apiNameWithType;
     }

     public List<String> buildGetterAndSetterNamesFromField(FieldMetadata field){
        String getter = "get" + field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
         String setter = "set" + field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
         List<String> getterAndSetterNames = new ArrayList<>(2);
         getterAndSetterNames.add(getter);
         getterAndSetterNames.add(setter);
         return getterAndSetterNames;
     }

    public String getPackageName() {
        return packageName;
    }

    public String getControllerClassName() {
        return controllerClassName;
    }

    public String getWebRequestClassName() {
        return webRequestClassName;
    }

    public String getWebRequestReferenceName() {
        return webRequestReferenceName;
    }

    public String getWebRequestDataClassName() {
        return webRequestDataClassName;
    }

    public String getWebRequestAttributesClassName() {
        return webRequestAttributesClassName;
    }

    public String getWebRequestAttributesReferenceName() {
        return webRequestAttributesReferenceName;
    }

    public List<AttributeConfig> getWebRequest() {
        return webRequest;
    }

    public String getWebResponseClassName() {
        return webResponseClassName;
    }

    public String getWebResponseReferenceName() {
        return webResponseReferenceName;
    }

    public String getWebResponseDataClassName() {
        return webResponseDataClassName;
    }

    public String getWebResponseAttributesClassName() {
        return webResponseAttributesClassName;
    }

    public String getWebResponseAttributesReferenceName() {
        return webResponseAttributesReferenceName;
    }

    public List<AttributeConfig> getWebResponse() {
        return webResponse;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public String getServiceResponsePackageName() {
        return serviceResponsePackageName;
    }

    public String getServiceClassName() {
        return serviceClassName;
    }

    public String getServiceReferenceName() {
        return serviceReferenceName;
    }

    public String getServiceResponseClassName() {
        return serviceResponseClassName;
    }

    public String getServiceImplClassName() {
        return serviceImplClassName;
    }

    public String getServiceResponseReferenceName() {
        return serviceResponseReferenceName;
    }

    public String getServiceResponseAttributesClassName() {
        return serviceResponseAttributesClassName;
    }

    public String getServiceResponseAttributesReferenceName() {
        return serviceResponseAttributesReferenceName;
    }

    public String getApiName() {
        return apiName;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getIdentifierNames() {
        return identifierNames;
    }

    public Map<String, List<String>> getRequestParams() {
        return requestParams;
    }

    public APITYPE getApiType() {
        return apiType;
    }

    public ENDPOINTOPERATION getEndpointOperation() {
        return endpointOperation;
    }

    public String getEventClassName() {
        return eventClassName;
    }

    public String getEventReferenceName() {
        return eventReferenceName;
    }

    public String getEventListenerClassName() {
        return eventListenerClassName;
    }

    public String getEventStoreClassName() {
        return eventStoreClassName;
    }

    public String getEventStoreImplClassName() {
        return eventStoreImplClassName;
    }

    public String getEventStoreReferenceName() {
        return eventStoreReferenceName;
    }

    public String getEventStoreRequestClassName() {
        return eventStoreRequestClassName;
    }

    public String getEventStoreRequestReferenceName() {
        return eventStoreRequestReferenceName;
    }

    public String getEventStoreResponseClassName() {
        return eventStoreResponseClassName;
    }

    public String getCommandClassName() {
        return commandClassName;
    }

    public String getGatewayPackageName() {
        return gatewayPackageName;
    }

    public String getCommandClassReferenceName() {
        return commandClassReferenceName;
    }

    public String getCommandResponseClassName() {
        return commandResponseClassName;
    }

    public String getCommandResponseClassReferenceName() {
        return commandResponseClassReferenceName;
    }

    public List<EndpointRequestParamConfig> getEndpointRequestParams() {
        return endpointRequestParams;
    }

    public String getApiResponseTypeValue() {
        return apiResponseTypeValue;
    }

    public String getExceptionHandlerName() {
        return exceptionHandlerName;
    }
}
