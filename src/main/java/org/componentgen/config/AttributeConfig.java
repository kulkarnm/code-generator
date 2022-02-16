package org.componentgen.config;

import java.util.ArrayList;
import java.util.List;

public class AttributeConfig {
    private String attributeName;
    private String attributeType;
    private List<AttributeConfig> children;
    public AttributeConfig(String attributeName,String attributeType,List<AttributeConfig> children){
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.children = children;
    }

    public AttributeConfig(){
        this.children = new ArrayList<>();
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public List<AttributeConfig> getChildren() {
        return children;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public void setChildren(List<AttributeConfig> children) {
        this.children = children;
    }

    public void addToChildren(AttributeConfig attributeConfig){
        this.children.add(attributeConfig);
    }

    public void set(String key,String value){
        if(key.equals("attributeName") ){
            this.setAttributeName(value);
        }else if(key.equals("attributeType")){
            this.setAttributeType(value);
        }
    }
}
