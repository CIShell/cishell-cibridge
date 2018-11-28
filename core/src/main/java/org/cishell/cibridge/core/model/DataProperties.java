package org.cishell.cibridge.core.model;

import java.util.List;

public class DataProperties {
    private String format;
    private String name;
    private String label;
    private String parent;
    private DataType type;
    private List<PropertyInput> otherProperties;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public List<PropertyInput> getOtherProperties() {
        return otherProperties;
    }

    public void setOtherProperties(List<PropertyInput> otherProperties) {
        this.otherProperties = otherProperties;
    }

    @Override
    public String toString() {
        return "DataProperties{" +
                "format='" + format + '\'' +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", parent='" + parent + '\'' +
                ", type=" + type +
                ", otherProperties=" + otherProperties +
                '}';
    }
}
