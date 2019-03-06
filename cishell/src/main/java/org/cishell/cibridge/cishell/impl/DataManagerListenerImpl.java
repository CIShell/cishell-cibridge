package org.cishell.cibridge.cishell.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
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
    private ConnectableObservable<org.cishell.cibridge.core.model.Data> dataAddedObservable;
    private ObservableEmitter<org.cishell.cibridge.core.model.Data> dataAddedObservableEmitter;
    private ConnectableObservable<org.cishell.cibridge.core.model.Data> dataRemovedObservable;
    private ObservableEmitter<org.cishell.cibridge.core.model.Data> dataRemovedObservableEmitter;


    public DataManagerListenerImpl() {
        Observable<org.cishell.cibridge.core.model.Data> dataaddedobservable = Observable.create(emitter -> {
            dataAddedObservableEmitter = emitter;

        });
        dataAddedObservable = dataaddedobservable.share().publish();
        dataAddedObservable.connect();

        Observable<org.cishell.cibridge.core.model.Data> dataremovedobservable = Observable.create(emitter -> {
            dataRemovedObservableEmitter = emitter;

        });
        dataRemovedObservable = dataremovedobservable.share().publish();
        dataRemovedObservable.connect();
    }

    public void setCIBridge(CIShellCIBridge ciBridge) {
        this.cibridge = ciBridge;
        this.cibridge.getDataManagerService().addDataManagerListener(this);
    }

    @Override
    public void dataAdded(Data data, String s) {

        // create cibridge data object which is a wrapper for cishell data object
        CIShellCIBridgeData cishellCIBridgeData = new CIShellCIBridgeData(data);

        // add the cibridge data object created to maps for easier access with its ID
        // and CIShellData
        cibridge.cishellData.getCIBridgeDataMap().put(cishellCIBridgeData.getId(), cishellCIBridgeData);
        cibridge.cishellData.getCIShellDataCIBridgeDataMap().put(data, cishellCIBridgeData);

        // update CIShellCIBridgeData properties with CIShellData properties
        updateCIShellCIBridgeDataProperties(cishellCIBridgeData, data);

        dataAddedObservableEmitter.onNext(cishellCIBridgeData);

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
                    String parentDataId = cibridge.cishellData.getCIShellDataCIBridgeDataMap()
                            .get((Data) metadata.get(PARENT)).getId();
                    cishellCIBridgeData.setParentDataId(parentDataId);
                default:
                    cishellCIBridgeData.getOtherProperties()
                            .add(new Property(propertyKey, metadata.get(propertyKey).toString()));
            }
        }

    }

    public ConnectableObservable<org.cishell.cibridge.core.model.Data> getDataAddedObservable() {
        return dataAddedObservable;
    }

    public ConnectableObservable<org.cishell.cibridge.core.model.Data> getDataRemovedObservable() {
        return dataRemovedObservable;
    }

    @Override
    public void dataLabelChanged(Data data, String label) {
        // update cibridge data label
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

        dataRemovedObservableEmitter.onNext(cishellCIBridgeData);
    }

    @Override
    public void dataSelected(Data[] data) {
        // todo this method should be deprecated now that there is no Eclipse UI
    }

}
