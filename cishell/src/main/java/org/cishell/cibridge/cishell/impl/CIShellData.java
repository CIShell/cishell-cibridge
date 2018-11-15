package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.framework.data.Data;

import java.util.Dictionary;
import java.util.Hashtable;

//todo refactor the class name to something more useful
public class CIShellData implements Data {
    private final Object data;
    private final String format;
    private final Dictionary<String, Object> properties = new Hashtable<>();

    public CIShellData(Object data, String format) {
        Preconditions.checkNotNull(data, "data cannot be null");
        Preconditions.checkNotNull(format, "data format cannot be null");

        this.data = data;
        this.format = format;
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
        return "CIShellData{" +
                "properties=" + properties +
                ", data=" + data +
                ", format='" + format + '\'' +
                '}';
    }
}