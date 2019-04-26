package org.cishell.cibridge.core.model;

import org.w3c.dom.Attr;

public class Property {
    private final String key;
    private final String value;
    private final AttributeType attributeType;

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
        this.attributeType = AttributeType.STRING;
    }

    public Property(String key, String value, AttributeType attributeType) {
        this.key = key;
        this.value = value;
        this.attributeType = attributeType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    @Override
    public String toString() {
        return "Property{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", attributeType=" + attributeType +
                '}';
    }
}
