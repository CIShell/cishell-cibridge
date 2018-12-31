package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FilenameUtils;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//TODO: need to test the datafacade implementation
public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {
    private CIShellCIBridge cibridge;
    private final Map<String, CIShellCIBridgeData> dataCache = new LinkedHashMap<>();

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkArgument(cibridge != null, "CIBridge cannot be null");
        this.cibridge = cibridge;
    }

    @Override
    public String validateData(String algorithmDefinitionId, List<String> dataIds) {
        return null;
    }

    @Override
    public List<AlgorithmInstance> findConverters(String dataId, String outFormat) {
        return null;
    }

    @Override
    public List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat) {
        return null;
    }

    @Override
    public DataQueryResults getData(DataFilter filter) {
        Preconditions.checkNotNull(filter, "filter cannot be null");

        List<Predicate<Data>> criteria = new ArrayList<>();

        //predicate on data id
        if (filter.getDataIds() != null) {
            criteria.add(data -> {
                if (data == null) return false;
                return filter.getDataIds().contains(data.getId());
            });
        }

        //predicate on data format
        if (filter.getFormats() != null) {
            criteria.add(data -> {
                if (data == null) return false;
                return filter.getFormats().contains(data.getFormat());
            });
        }

        //predicate on data type
        if (filter.getTypes() != null) {
            criteria.add(data -> {
                if (data == null) return false;
                return data.getType() != null && filter.getTypes().contains(data.getType());
            });
        }

        //predicate on isModified
        if (filter.getModified() != null) {
            criteria.add(data -> {
                if (data == null) return false;
                return data.getModified() != null && filter.getModified().booleanValue() == data.getModified().booleanValue();
            });
        }

        //predicate on otherProperties
        if (filter.getProperties() != null) {
            criteria.add(data -> {
                if (data == null) return false;
                if (data.getOtherProperties() == null) return false;
                Map<String, String> propertyMap = data.getOtherProperties().stream().collect(Collectors.toMap(Property::getKey, Property::getValue));
                boolean satisfied = true;
                for (PropertyInput propertyInput : filter.getProperties()) {
                    if (!(propertyMap.containsKey(propertyInput.getKey()) && propertyMap.get(propertyInput.getKey()).equals(propertyInput.getValue()))) {
                        satisfied = false;
                        break;
                    }
                }
                return satisfied;
            });

        }

        //create pagination related data
        int offset = DataQueryResults.DEFAULT_OFFSET;
        int limit = DataQueryResults.DEFAULT_LIMIT;
        boolean hasNextPage = false;
        boolean hasPreviousPage = false;
        if (filter.getOffset() > 0) {
            offset = filter.getOffset();
        }

        if (filter.getLimit() > 0) {
            limit = filter.getLimit();
        }

        //filter based on criteria
        List<Data> dataList = new ArrayList<>();
        for (Map.Entry<String, CIShellCIBridgeData> entry : dataCache.entrySet()) {
            CIShellCIBridgeData data = entry.getValue();
            boolean satisfied = true;

            for (Predicate<Data> criterion : criteria) {
                if (!criterion.test(data)) {
                    satisfied = false;
                    break;
                }
            }

            if (satisfied) {
                if (offset == 0) {
                    if (limit > 0) {
                        dataList.add(data);
                        limit--;
                    } else {
                        hasNextPage = true;
                        break;
                    }

                } else {
                    hasPreviousPage = true;
                    offset--;
                }
            }
        }

        return new DataQueryResults(dataList, new PageInfo(hasNextPage, hasPreviousPage));
    }

    @Override
    public String downloadData(String dataId) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkArgument(dataCache.containsKey(dataId), "Invalid dataId. No data object found with dataId '%s'", dataId);
        CIShellCIBridgeData data = dataCache.get(dataId);
        return data.getCIShellData().getData().toString();
    }

    /* Mutations */
    //todo should the API user pass the data format or should we auto-detect it? that has bugged me for so long
    @Override
    public Data uploadData(String filePath, DataProperties properties) {
        Preconditions.checkNotNull(filePath, "File path cannot be null");
        filePath = filePath.trim();
        File file = new File(filePath);
        Preconditions.checkArgument(file.exists(), "'%s' doesn't exist", filePath);
        Preconditions.checkArgument(file.isFile(), "'%s' is not a file", filePath);

        //if format is specified in properties then set it else parse it from the arguments
        String format;
        if (properties != null && properties.getFormat() != null) {
            format = properties.getFormat();
        } else {
            //todo is this the correct way of setting the format?
            format = "file-ext:" + FilenameUtils.getExtension(filePath);
        }

        //create cibridge data object which is a wrapper for cishell data object
        CIShellCIBridgeData data = new CIShellCIBridgeData(file, format);

        //add the cibridge data object created to map for easier access with its id
        dataCache.put(data.getId(), data);

        //add properties
        if (properties != null) {
            updateData(data.getId(), properties);
        }

        //add the CIShellData object wrapped inside CIShellCIBridgeData to data manager service
        cibridge.getDataManagerService().addData(data.getCIShellData());

        return data;
    }

    @Override
    public Boolean removeData(String dataId) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        if (!dataCache.containsKey(dataId)) {
            //todo convert below statement into a log entry
            //System.out.println("Invalid dataId. No data present with dataId '" + dataId + "'");
            return false;
        }

        CIShellCIBridgeData data = dataCache.get(dataId);
        dataCache.remove(dataId);
        cibridge.getDataManagerService().removeData(data.getCIShellData());
        return true;
    }

    @Override
    public Boolean updateData(String dataId, DataProperties properties) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkArgument(dataCache.containsKey(dataId), "Invalid dataId. No data object found with dataId '%s'", dataId);
        Preconditions.checkNotNull(properties);
        //todo add logic path to return boolean by adding log entries here

        CIShellCIBridgeData data = dataCache.get(dataId);

        //update CIBridgeData
        if (properties.getName() != null) {
            data.setName(properties.getName());
        }

        if (properties.getLabel() != null) {
            data.setLabel(properties.getLabel());
        }

        if (properties.getType() != null) {
            data.setType(properties.getType());
        }

        if (properties.getParent() != null) {
            data.setParentDataId(properties.getParent());
            //todo what is the scope of this field. is it useful for cishell? I guess not.
        }

        if (properties.getOtherProperties() != null) {
            for (PropertyInput propertyInput : properties.getOtherProperties()) {
                for (Property property : data.getOtherProperties()) {
                    if (property.getKey().equalsIgnoreCase(propertyInput.getKey())) {
                        data.getOtherProperties().remove(property);
                        break;
                    }
                }
                data.getOtherProperties().add(new Property(propertyInput.getKey(), propertyInput.getValue()));
            }
        }

        //update CIShell data wrapped inside CIBridge data
        org.cishell.framework.data.Data ciShellData = data.getCIShellData();

        if (properties.getType() != null) {
            ciShellData.getMetadata().put("Type", properties.getType().name());
        }

        if (properties.getLabel() != null) {
            ciShellData.getMetadata().put("Label", properties.getLabel());
        }

        if (properties.getName() != null) {
            ciShellData.getMetadata().put("Name", properties.getName());
        }

        if (properties.getOtherProperties() != null) {
            for (PropertyInput propertyInput : properties.getOtherProperties()) {
                ciShellData.getMetadata().put(propertyInput.getKey(), propertyInput.getValue());
            }
        }
        //todo do we need to set other data properties? and do we need remove any of the above setters?

        return true;
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

    public Map<String, CIShellCIBridgeData> getDataCache() {
        return dataCache;
    }

}
