package org.cishell.cibridge.core.model;

import java.util.List;

public class DataFilter {
    private List<String> dataIds;
    private List<String> formats;
    private Boolean isModified;
    private List<DataType> types;
    private List<PropertyInput> properties;
    private int limit;
    private int offset;

    public List<String> getDataIds() {
        return dataIds;
    }

    public void setDataIds(List<String> dataIds) {
        this.dataIds = dataIds;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }

    public Boolean getModified() {
        return isModified;
    }

    public void setModified(Boolean modified) {
        isModified = modified;
    }

    public List<DataType> getTypes() {
        return types;
    }

    public void setTypes(List<DataType> types) {
        this.types = types;
    }

    public List<PropertyInput> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInput> properties) {
        this.properties = properties;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
