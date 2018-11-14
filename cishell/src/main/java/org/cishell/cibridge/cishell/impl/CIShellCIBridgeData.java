package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataProperties;
import org.cishell.cibridge.core.model.Property;
import org.cishell.cibridge.core.model.PropertyInput;

public class CIShellCIBridgeData extends Data {
    private final org.cishell.framework.data.Data data;

    public CIShellCIBridgeData(org.cishell.framework.data.Data data) {
        this(data, null);
    }

    public CIShellCIBridgeData(org.cishell.framework.data.Data data, DataProperties properties) {
        super(generateAndGetUniqueID());
        Preconditions.checkNotNull(data, "Data cannot be null");
        Preconditions.checkNotNull(data.getFormat(), "Data format cannot be null");

        this.data = data;
        this.format = data.getFormat();

        if (properties != null) {
            updateProperties(properties);
        }
    }

    public org.cishell.framework.data.Data getCIShellData() {
        return data;
    }

    public void updateProperties(DataProperties properties) {

        if (properties.getName() != null) {
            this.name = properties.getName();
        }

        if (properties.getLabel() != null) {
            this.label = properties.getLabel();
        }

        if (properties.getType() != null) {
            this.type = properties.getType();
        }

        if (properties.getFormat() != null) {
            this.format = properties.getFormat();
        }

        if (properties.getParent() != null) {
            this.parentDataId = properties.getParent();
            //todo what is the scope of this field. is it useful for cishell? I guess not.
        }

        if (properties.getOtherProperties() != null) {
            for (PropertyInput property : properties.getOtherProperties()) {
                //todo this could potentially add multiple values against a single key if misused
                otherProperties.add(new Property(property.getKey(), property.getValue()));
            }
        }

        if (data instanceof CIShellDataImpl) {
            CIShellDataImpl ciShellData = (CIShellDataImpl) data;
            ciShellData.updateProperties(properties);
        }
    }

    private static long instanceCounter = 0;

    //todo adding rudimentary way of generating unique ids. improve it later on
    private static String generateAndGetUniqueID() {
        return ++instanceCounter + "";
    }

    @Override
    public String toString() {
        return "CIShellCIBridgeData{" +
                "data=" + data +
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
