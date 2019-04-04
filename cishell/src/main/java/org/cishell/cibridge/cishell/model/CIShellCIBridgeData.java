package org.cishell.cibridge.cishell.model;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.core.model.Data;

public class CIShellCIBridgeData extends Data {

    private org.cishell.framework.data.Data ciShellData;

    public CIShellCIBridgeData(org.cishell.framework.data.Data data) {

        super(generateAndGetUniqueID());
        Preconditions.checkNotNull(data, "data cannot be null");
        this.ciShellData = data;
        this.format = this.ciShellData.getFormat();


    }

    public org.cishell.framework.data.Data getCIShellData() {
        return ciShellData;
    }

    private static long instanceCounter = 0;

    //todo adding rudimentary way of generating unique ids. Should be UUIDs
    private static String generateAndGetUniqueID() {
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "CIShellCIBridgeData{" +
                "ciShellData=" + ciShellData +
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
