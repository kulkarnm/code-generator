package org.componentgen.config;

public class RequestParamConfig {
    private String requestParamName;
    private String requestParamValue;

    public RequestParamConfig(String requestParamName, String requestParamValue) {
        this.requestParamName = requestParamName;
        this.requestParamValue = requestParamValue;
    }

    public RequestParamConfig() {
    }

    public String getRequestParamName() {
        return requestParamName;
    }

    public void setRequestParamName(String requestParamName) {
        this.requestParamName = requestParamName;
    }

    public String getRequestParamValue() {
        return requestParamValue;
    }

    public void setRequestParamValue(String requestParamValue) {
        this.requestParamValue = requestParamValue;
    }

    public void set(String key, String value) {
        if (key.equals("requestParamName")) {
            this.setRequestParamName(value);
        } else if (key.equals("requestParamValue")) {
            this.setRequestParamValue(value);
        }
    }
}
