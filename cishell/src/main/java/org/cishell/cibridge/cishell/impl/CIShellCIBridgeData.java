package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.core.model.Data;

import java.io.File;

public class CIShellCIBridgeData extends Data {
    private final org.cishell.framework.data.Data ciShellData;

    public CIShellCIBridgeData(File file, String format) {
        super(generateAndGetUniqueID(), format);
        Preconditions.checkNotNull(file, "File cannot be null");
        Preconditions.checkNotNull(format, "File format cannot be null");

        //create CIShellData object which is an implementation of CIShell frameworks's Data interface
        this.ciShellData = new CIShellData(file, format);
    }

    public org.cishell.framework.data.Data getCIShellData() {
        return ciShellData;
    }

    private static long instanceCounter = 0;

    //todo adding rudimentary way of generating unique ids. improve it later on
    private static String generateAndGetUniqueID() {
        return ++instanceCounter + "";
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
