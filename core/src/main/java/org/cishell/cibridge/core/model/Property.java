package org.cishell.cibridge.core.model;

public class Property {
    private final String key;
    private final String value;

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Property{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
