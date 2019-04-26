package org.cishell.cibridge.core.model;

public class PropertyInput {
    private String key;
    private String value;
    private AttributeType attributeType;

    public PropertyInput() {

    }

    public PropertyInput(String key, String value) {
        this.key = key;
        this.value = value;
        this.attributeType = AttributeType.STRING;
    }

    public PropertyInput(String key, String value, AttributeType attributeType) {
        this.key = key;
        this.value = value;
        this.attributeType = attributeType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return "PropertyInput{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", attributeType=" + attributeType +
                '}';
    }
}
