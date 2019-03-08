package org.cishell.cibridge.cishell.impl;

import org.cishell.app.service.datamanager.DataManagerListener;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.model.DataType;
import org.cishell.cibridge.core.model.Property;
import org.cishell.framework.data.Data;

import java.util.Dictionary;
import java.util.Enumeration;

import static org.cishell.framework.data.DataProperty.*;

public class DataManagerListenerImpl implements DataManagerListener {
    private CIShellCIBridge cibridge;

    public void setCIBridge(CIShellCIBridge ciBridge) {
        this.cibridge = ciBridge;
        this.cibridge.getDataManagerService().addDataManagerListener(this);
    }

    @Override
    public void dataAdded(Data data, String s) {
        //create cibridge data object which is a wrapper for cishell data object
        CIShellCIBridgeData cishellCIBridgeData = new CIShellCIBridgeData(data);

        //add the cibridge data object created to maps for easier access with its ID and CIShellData
        cibridge.cishellData.getCIBridgeDataMap().put(cishellCIBridgeData.getId(), cishellCIBridgeData);
        cibridge.cishellData.getCIShellDataCIBridgeDataMap().put(data, cishellCIBridgeData);

        //update CIShellCIBridgeData properties with CIShellData properties
        updateCIShellCIBridgeDataProperties(cishellCIBridgeData, data);
    }

    private void updateCIShellCIBridgeDataProperties(CIShellCIBridgeData cishellCIBridgeData, Data data) {
        cishellCIBridgeData.setFormat(data.getFormat());
        Dictionary<String, Object> metadata = data.getMetadata();

        Enumeration<String> metadataIterator = metadata.keys();

        while (metadataIterator.hasMoreElements()) {
            String propertyKey = metadataIterator.nextElement();
            switch (propertyKey) {
                case LABEL:
                    cishellCIBridgeData.setLabel(metadata.get(LABEL).toString());
                    break;
                case "Name":
                    cishellCIBridgeData.setName(metadata.get("Name").toString());
                    break;
                case TYPE:
                    cishellCIBridgeData.setType(DataType.valueOf(metadata.get(TYPE).toString().toUpperCase()));
                    break;
                case MODIFIED:
                    cishellCIBridgeData.setModified(Boolean.valueOf(metadata.get(MODIFIED).toString()));
                    break;
                case PARENT:
                    String parentDataId = cibridge.cishellData.getCIShellDataCIBridgeDataMap().get((Data) metadata.get(PARENT)).getId();
                    cishellCIBridgeData.setParentDataId(parentDataId);
                default:
                    cishellCIBridgeData.getOtherProperties().add(new Property(propertyKey, metadata.get(propertyKey).toString()));
            }
        }


    }

    @Override
    public void dataLabelChanged(Data data, String label) {
        //update cibridge data label
        CIShellCIBridgeData ciBridgeData = cibridge.cishellData.getCIShellDataCIBridgeDataMap().get(data);
        if (label != null) {
            ciBridgeData.setLabel(label);
        }
    }

    @Override
    public void dataRemoved(Data data) {
        CIShellCIBridgeData cishellCIBridgeData = cibridge.cishellData.getCIShellDataCIBridgeDataMap().get(data);
        if (cishellCIBridgeData != null) {
            cibridge.cishellData.getCIBridgeDataMap().remove(cishellCIBridgeData.getId());
        }
        cibridge.cishellData.getCIShellDataCIBridgeDataMap().remove(data);

    }

    @Override
    public void dataSelected(Data[] data) {
        //not relevant for this implementation
    }

}
