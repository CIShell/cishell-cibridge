package org.cishell.cibridge.core.model;

import java.util.LinkedList;
import java.util.List;

public class Data {
    private final String id;
    protected final String format;
    protected String name;
    protected String label;
    protected String parentDataId;
    protected DataType type;
    protected Boolean isModified = false;
    protected final List<Property> otherProperties = new LinkedList<>();

    public Data(String id, String format) {
        this.id = id;
        this.format = format;
    }

    public String getId() {
        return id;
    }

    public String getFormat() {
        return format;
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

    public String getParentDataId() {
        return parentDataId;
    }

    public void setParentDataId(String parentDataId) {
        this.parentDataId = parentDataId;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Boolean getModified() {
        return isModified;
    }

    public void setModified(Boolean modified) {
        isModified = modified;
    }

    public List<Property> getOtherProperties() {
        return otherProperties;
    }

    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", format='" + format + '\'' +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", parentDataId='" + parentDataId + '\'' +
                ", type=" + type +
                ", isModified=" + isModified +
                ", otherProperties=" + otherProperties +
                '}';
    }
}
