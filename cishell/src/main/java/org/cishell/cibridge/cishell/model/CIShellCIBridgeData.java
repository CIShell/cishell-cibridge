package org.cishell.cibridge.cishell.model;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataType;
import org.cishell.cibridge.core.model.Property;

import java.util.Dictionary;
import java.util.Enumeration;

import static org.cishell.framework.data.DataProperty.*;

public class CIShellCIBridgeData extends Data {

    private org.cishell.framework.data.Data cishellData;

    public CIShellCIBridgeData(org.cishell.framework.data.Data data, String parentDataId) {

        super(generateAndGetUniqueID());
        Preconditions.checkNotNull(data, "data cannot be null");
        this.cishellData = data;

        setFormat(cishellData.getFormat());
        setParentDataId(parentDataId);

        Dictionary<String, Object> metadata = data.getMetadata();

        if (metadata != null) {
            Enumeration<String> metadataIterator = metadata.keys();

            while (metadataIterator.hasMoreElements()) {
                String propertyKey = metadataIterator.nextElement();
                switch (propertyKey) {
                    case LABEL:
                        setLabel(metadata.get(LABEL).toString());
                        break;
                    case "Name":
                        setName(metadata.get("Name").toString());
                        break;
                    case TYPE:
                        setType(DataType.valueOf(metadata.get(TYPE).toString().toUpperCase()));
                        break;
                    case MODIFIED:
                        setModified(Boolean.valueOf(metadata.get(MODIFIED).toString()));
                        break;
                    case PARENT:
                        //parent already set above
                        break;
                    default:
                        getOtherProperties().add(new Property(propertyKey, metadata.get(propertyKey).toString()));
                }
            }
        }

    }

    public org.cishell.framework.data.Data getCIShellData() {
        return cishellData;
    }

    private static String generateAndGetUniqueID() {
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "CIShellCIBridgeData{" +
                "cishellData=" + cishellData +
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
