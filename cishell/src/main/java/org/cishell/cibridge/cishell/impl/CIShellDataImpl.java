package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.core.model.DataProperties;
import org.cishell.framework.data.Data;

import java.util.Dictionary;
import java.util.Hashtable;

//todo refactor the class name to something more useful
public class CIShellDataImpl implements Data {
    private final Dictionary<String, Object> properties;
    private Object data;
    private String format;

    public CIShellDataImpl(Object data, String format) {
        this(data, format, null);
    }

    public CIShellDataImpl(Object data, String format, DataProperties dataProperties) {
        Preconditions.checkNotNull(data, "data cannot be null");
        Preconditions.checkNotNull(format, "data format cannot be null");

        this.data = data;
        this.format = format;
        this.properties = new Hashtable<>();

        if (dataProperties != null) {
            //todo how to parse properties? what is the scope of data properties passed in cibridge?
        }

    }

    public Object getData() {
        return data;
    }

    public Dictionary<String, Object> getMetadata() {
        return properties;
    }

    public String getFormat() {
        return format;
    }

    public String toString() {
        return "CIShellDataImpl{" +
                "properties=" + properties +
                ", data=" + data +
                ", format='" + format + '\'' +
                '}';
    }
}