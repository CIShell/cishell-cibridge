package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FilenameUtils;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.util.PaginationUtil;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {
    private CIShellCIBridge cibridge;
    //todo manybe convert this to list since we are not using keys directly
    private final Map<String, CIShellCIBridgeData> dataCache = new LinkedHashMap<>();

    public void setCIBridge(CIShellCIBridge cibridge) {
        Preconditions.checkNotNull(cibridge, "CIBridge cannot be null");
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
            Map<String, Set<String>> propertyValuesMap = new HashMap<>();
            filter.getProperties().forEach(propertyInput -> {
                if (!propertyValuesMap.containsKey(propertyInput.getKey())) {
                    propertyValuesMap.put(propertyInput.getKey(), new HashSet<>());
                }
                propertyValuesMap.get(propertyInput.getKey()).add(propertyInput.getValue());
            });

            for (Map.Entry<String, Set<String>> entry : propertyValuesMap.entrySet()) {
                criteria.add(data -> {
                    String key = entry.getKey();
                    if (data == null) return false;
                    Map<String, String> otherProperties = data.getOtherProperties().stream().collect(Collectors.toMap(Property::getKey, Property::getValue));
                    if (otherProperties.containsKey(key)) {
                        return entry.getValue().contains(otherProperties.get(key));
                    }
                    return false;
                });
            }

        }

        QueryResults<Data> paginatedQueryResults = PaginationUtil.getPaginatedResults(
                new ArrayList<>(dataCache.values()), criteria, filter.getOffset(), filter.getLimit());

        return new DataQueryResults(paginatedQueryResults.getResults(), paginatedQueryResults.getPageInfo());
    }

    @Override
    public String downloadData(String dataId) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkArgument(dataCache.containsKey(dataId), "Invalid dataId. No data object found with dataId '%s'", dataId);
        return dataCache.get(dataId).getCIShellData().getData().toString();
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
        Preconditions.checkNotNull(properties, "dataProperties cannot be null");
        Preconditions.checkArgument(dataCache.containsKey(dataId), "Invalid dataId. No data object found with dataId '%s'", dataId);

        CIShellCIBridgeData datum = dataCache.get(dataId);

        //update CIBridgeData
        if (properties.getName() != null) {
            datum.setName(properties.getName());
        }

        if (properties.getLabel() != null) {
            datum.setLabel(properties.getLabel());
        }

        if (properties.getType() != null) {
            datum.setType(properties.getType());
        }

        if (properties.getParent() != null) {
            datum.setParentDataId(properties.getParent());
            //todo what is the scope of this field. is it useful for cishell? I guess not.
        }

        if (properties.getOtherProperties() != null) {
            for (PropertyInput propertyInput : properties.getOtherProperties()) {
                for (Property property : datum.getOtherProperties()) {
                    //todo should we use equals or equalignorecase??
                    if (property.getKey().equals(propertyInput.getKey())) {
                        datum.getOtherProperties().remove(property);
                        break;
                    }
                }
                datum.getOtherProperties().add(new Property(propertyInput.getKey(), propertyInput.getValue()));
            }
        }

        //update CIShell data wrapped inside CIBridge data
        org.cishell.framework.data.Data ciShellData = datum.getCIShellData();

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
            properties.getOtherProperties()
                    .forEach(propertyInput -> ciShellData.getMetadata().put(propertyInput.getKey(), propertyInput.getValue()));
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

    Map<String, CIShellCIBridgeData> getDataCache() {
        return dataCache;
    }

}
