package org.cishell.cibridge.core.model;

public class PropertyInput {
    private String key;
    private String value;

    public PropertyInput() {

    }

    public PropertyInput(String key, String value) {
        this.key = key;
        this.value = value;
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

    @Override
    public String toString() {
        return "PropertyInput{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
