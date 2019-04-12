package org.cishell.cibridge.cishell.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.cishell.app.service.datamanager.DataManagerListener;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeData;
import org.cishell.framework.data.Data;

import static org.cishell.framework.data.DataProperty.PARENT;

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

        //get parent id
        String parentDataId = null;
        if (data.getMetadata() != null && data.getMetadata().get(PARENT) != null) {
            Data parent = (Data) data.getMetadata().get(PARENT);
            parentDataId = cibridge.cishellData.getCIShellDataCIBridgeDataMap().get(parent).getId();
        }

        // create cibridge data object which is a wrapper for cishell data object
        CIShellCIBridgeData cishellCIBridgeData = new CIShellCIBridgeData(data, parentDataId);

        // add the cibridge data object created to maps for easier access with its ID
        // and CIShellData
        cibridge.cishellData.getCIBridgeDataMap().put(cishellCIBridgeData.getId(), cishellCIBridgeData);
        cibridge.cishellData.getCIShellDataCIBridgeDataMap().put(data, cishellCIBridgeData);

        dataAddedObservableEmitter.onNext(cishellCIBridgeData);
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
        //not relevant for this implementation
    }

}
