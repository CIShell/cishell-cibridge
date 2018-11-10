package org.cishell.cibridge.core.model;

import com.google.common.base.Preconditions;

import java.util.LinkedList;
import java.util.List;

public class Data {
    private final String id;
    private final org.cishell.framework.data.Data data;
    private String format;
    private String name;
    private String label;
    private String parentDataId;
    private DataType type;
    private Boolean isModified;
    private final List<Property> otherProperties;

    public Data(org.cishell.framework.data.Data data) {
        this(data, null);
    }

    public Data(org.cishell.framework.data.Data data, DataProperties properties) {
        Preconditions.checkNotNull(data, "Data cannot be null");
        Preconditions.checkNotNull(data.getFormat(), "Data format cannot be null");

        id = getUniqueID();
        this.data = data;
        this.format = data.getFormat();
        this.isModified = false;
        this.otherProperties = new LinkedList<>();

        if (properties != null) {
            this.name = properties.getName();
            this.label = properties.getLabel();
            //todo what is the scope of this field. is it useful for cishell? I guess not.
            //this.parentDataId = properties.getParent();
            this.type = properties.getType();

            if (properties.getOtherProperties() != null) {
                for (PropertyInput property : properties.getOtherProperties()) {
                    otherProperties.add(new Property(property.getKey(), property.getValue()));
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public org.cishell.framework.data.Data getCIShellData() {
        return data;
    }

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

    private static long instanceCounter = 0;

    //todo adding rudimentary way of generating unique ids. improve it later on
    private static String getUniqueID() {
        return ++instanceCounter + "";
    }

    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", data=" + data +
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
