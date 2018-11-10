package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FilenameUtils;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: need to test the datafacade implementation
public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {
    private CIShellCIBridge cibridge;
    private final Map<String, Data> dataMap = new HashMap<>();

    public void setCIBridge(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
    }

    @Override
    public String validateData(String algorithmDefinitionId, List<String> dataIds) {
        return null;
    }

    private org.cishell.framework.data.Data[] getRealData(List<String> dataIds) {
        return null;
    }

    @Override //complete
    public List<AlgorithmInstance> findConverters(String dataId, String outFormat) {
        return null;
    }

    @Override
    public List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat) {
        return null;
    }

    @Override
    public DataQueryResults getData(DataFilter filter) {
        return null;
    }

    @Override
    public String downloadData(String dataId) {
        return null;
    }

    /* Mutations */
    //todo should the API user pass the data format or should we auto-detect it? that has bugged me for so long
    @Override
    public Data uploadData(String filePath, DataProperties properties) {
        System.out.println("enters uploadData");
        Preconditions.checkNotNull(filePath, "filePath cannot be null");
        filePath = filePath.trim();

        //if format is specified in properties then set it else parse it from filepath
        String format;
        if (properties != null && properties.getFormat() != null) {
            format = properties.getFormat();
        } else {
            //todo is this the correct way of setting the format?
            format = "file-ext:" + FilenameUtils.getExtension(filePath);
        }

        //create data object which is an implementation of cishell frameworks's Data interface
        CIShellDataImpl ciShellData = new CIShellDataImpl(new File(filePath), format, properties);

        System.out.println("created ciShellData");

        //add the cishell data object to data manager service
        cibridge.getDataManagerService().addData(ciShellData);

        //create cibridge data object which is a wrapper for cishell data object
        Data data = new Data(ciShellData, properties);

        //add the cibridge data object created to map for easier access with its id
        dataMap.put(data.getId(), data);

        return data;
    }

    @Override
    public Boolean removeData(String dataId) {
        return null;
    }

    @Override
    public Boolean updateData(String dataId, DataProperties properties) {
        return null;
    }

    //TODO: below are unimplemented subscriptions of DataFacade
    @Override
    public Data dataAdded() {

        return null;
    }

    @Override
    public Data dataRemoved() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Data dataUpdated() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

}
