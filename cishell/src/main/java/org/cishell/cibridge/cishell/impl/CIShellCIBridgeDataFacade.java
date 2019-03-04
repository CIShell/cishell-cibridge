package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.apache.commons.io.FilenameUtils;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.util.PaginationUtil;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.conversion.Converter;
import org.reactivestreams.Publisher;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.cishell.framework.data.DataProperty.*;

public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {

    private CIShellCIBridge cibridge;
    private DataManagerListenerImpl dataManagerListener = new DataManagerListenerImpl();
    private final Map<String, CIShellCIBridgeData> cibridgeDataMap = new LinkedHashMap<>();
    private final Map<org.cishell.framework.data.Data, CIShellCIBridgeData> cishellDataCIBridgeDataMap = new HashMap<>();
    private ConnectableObservable<org.cishell.cibridge.core.model.Data> dataUpdatedObservable;
    private ObservableEmitter<org.cishell.cibridge.core.model.Data> dataUpdatedObservableEmitter;

    public void setCIBridge(CIShellCIBridge ciBridge) {
        Preconditions.checkNotNull(ciBridge, "CIBridge cannot be null");
        this.cibridge = ciBridge;
        this.dataManagerListener.setCIBridge(ciBridge);
    }

    public CIShellCIBridgeDataFacade() {
        Observable<org.cishell.cibridge.core.model.Data> dataupdatedobservable = Observable.create(emitter -> {
            dataUpdatedObservableEmitter = emitter;

        });
        dataUpdatedObservable = dataupdatedobservable.share().publish();
        dataUpdatedObservable.connect();
    }

    @Override
    public String validateData(String algorithmDefinitionId, List<String> dataIds) {
        return null;
    }

    @Override
    public List<AlgorithmDefinition> findConverters(String dataId, String outFormat) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkNotNull(outFormat, "output format cannot be null");
        Preconditions.checkArgument(cibridgeDataMap.containsKey(dataId), "data with given dataId was not found");

        CIShellCIBridgeData cibridgeData = cibridgeDataMap.get(dataId);
        Converter[] converters = cibridge.getDataConversionService().findConverters(cibridgeData.getCIShellData(),
                outFormat);

        // TODO isn't the returned list should of algorithm definitions?

        return null;
    }

    @Override
    public List<AlgorithmDefinition> findConvertersByFormat(String inFormat, String outFormat) {
        Preconditions.checkNotNull(inFormat, "input format cannot be null");
        Preconditions.checkNotNull(outFormat, "output format cannot be null");

        Converter[] converters = cibridge.getDataConversionService().findConverters(inFormat, outFormat);

        return null;
    }

    @Override
    public DataQueryResults getData(DataFilter filter) {
        Preconditions.checkNotNull(filter, "filter cannot be null");

        List<Predicate<Data>> criteria = new ArrayList<>();

        // predicate on data id
        if (filter.getDataIds() != null) {
            criteria.add(data -> {
                if (data == null)
                    return false;
                return filter.getDataIds().contains(data.getId());
            });
        }

        // predicate on data format
        if (filter.getFormats() != null) {
            criteria.add(data -> {
                if (data == null)
                    return false;
                return filter.getFormats().contains(data.getFormat());
            });
        }

        // predicate on data type
        if (filter.getTypes() != null) {
            criteria.add(data -> {
                if (data == null)
                    return false;
                return data.getType() != null && filter.getTypes().contains(data.getType());
            });
        }

        // predicate on isModified
        if (filter.getModified() != null) {
            criteria.add(data -> {
                if (data == null)
                    return false;
                return data.getModified() != null
                        && filter.getModified().booleanValue() == data.getModified().booleanValue();
            });
        }

        // predicate on otherProperties
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
                    if (data == null)
                        return false;
                    Map<String, String> otherProperties = data.getOtherProperties().stream()
                            .collect(Collectors.toMap(Property::getKey, Property::getValue));
                    if (otherProperties.containsKey(key)) {
                        return entry.getValue().contains(otherProperties.get(key));
                    }
                    return false;
                });
            }

        }

        QueryResults<Data> paginatedQueryResults = PaginationUtil.getPaginatedResults(
                new ArrayList<>(cibridgeDataMap.values()), criteria, filter.getOffset(), filter.getLimit());

        return new DataQueryResults(paginatedQueryResults.getResults(), paginatedQueryResults.getPageInfo());
    }

    @Override
    public String downloadData(String dataId) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkArgument(cibridgeDataMap.containsKey(dataId),
                "Invalid dataId. No data object found with dataId '%s'", dataId);
        return cibridgeDataMap.get(dataId).getCIShellData().getData().toString();
    }

    /* Mutations */
    // todo should the API user pass the data format or should we auto-detect it?
    // that has bugged me for so long
    @Override
    public Data uploadData(String filePath, DataProperties properties) {
        Preconditions.checkNotNull(filePath, "File path cannot be null");
        filePath = filePath.trim();
        File file = new File(filePath);
        Preconditions.checkArgument(file.exists(), "'%s' doesn't exist", filePath);
        Preconditions.checkArgument(file.isFile(), "'%s' is not a file", filePath);

        // if format is specified in properties then set it else parse it from the
        // arguments
        String format;
        if (properties != null && properties.getFormat() != null) {
            format = properties.getFormat();
        } else {
            // todo is this the correct way of setting the format?
            format = "file-ext:" + FilenameUtils.getExtension(filePath);
        }

        // create CIShellData object which is an implementation of CIShell frameworks's
        // Data interface
        CIShellData cishellData = new CIShellData(file, format);

        if (properties != null) {
            updateCIShellDataProperties(cishellData, properties);
        }
        // add the CIShellData object wrapped inside CIShellCIBridgeData to data manager
        // service
        cibridge.getDataManagerService().addData(cishellData);

        return cishellDataCIBridgeDataMap.get(cishellData);
    }

    @Override
    public Boolean removeData(String dataId) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        if (!cibridgeDataMap.containsKey(dataId)) {
            return false;
        }

        cibridge.getDataManagerService().removeData(cibridgeDataMap.get(dataId).getCIShellData());
        return true;
    }

    @Override
    public Boolean updateData(String dataId, DataProperties properties) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkNotNull(properties, "dataProperties cannot be null");
        Preconditions.checkArgument(cibridgeDataMap.containsKey(dataId),
                "Invalid dataId. No data object found with dataId '%s'", dataId);

        CIShellCIBridgeData cishellCIBridgedata = cibridgeDataMap.get(dataId);

        // update CIShellCIBridgeData properties
        updateCIShellCIBridgeDataProperties(cishellCIBridgedata, properties);

        // also update properties of CIShellData object wrapped inside
        // CIShellCIBridgeData object
        updateCIShellDataProperties(cishellCIBridgedata.getCIShellData(), properties);

        dataUpdatedObservableEmitter.onNext(cishellCIBridgedata);

        return true;
    }

    private void updateCIShellCIBridgeDataProperties(CIShellCIBridgeData data, DataProperties properties) {
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
        }

        if (properties.getOtherProperties() != null) {
            for (PropertyInput propertyInput : properties.getOtherProperties()) {
                for (Property property : data.getOtherProperties()) {
                    if (property.getKey().equals(propertyInput.getKey())) {
                        data.getOtherProperties().remove(property);
                        break;
                    }
                }
                data.getOtherProperties().add(new Property(propertyInput.getKey(), propertyInput.getValue()));
            }
        }
    }

    private void updateCIShellDataProperties(org.cishell.framework.data.Data data, DataProperties properties) {

        if (properties.getType() != null) {
            data.getMetadata().put(TYPE, properties.getType().name());
        }

        if (properties.getLabel() != null) {
            data.getMetadata().put(LABEL, properties.getLabel());
        }

        if (properties.getName() != null) {
            data.getMetadata().put("Name", properties.getName());
        }

        if (properties.getParent() != null) {
            String parentDataId = properties.getParent();
            data.getMetadata().put(PARENT, cibridgeDataMap.get(parentDataId).getCIShellData());
        }

        if (properties.getOtherProperties() != null) {
            properties.getOtherProperties()
                    .forEach(propertyInput -> data.getMetadata().put(propertyInput.getKey(), propertyInput.getValue()));
        }
    }

    @Override
    public Publisher<Data> dataAdded() {
        Flowable<Data> publisher;
        ConnectableObservable<Data> connectableObservable = dataManagerListener.getDataAddedObservable();
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

    @Override
    public Publisher<Data> dataRemoved() {
        Flowable<Data> publisher;
        ConnectableObservable<Data> connectableObservable = dataManagerListener.getDataRemovedObservable();
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

    @Override
    public Publisher<Data> dataUpdated() {
        Flowable<Data> publisher;
        publisher = dataUpdatedObservable.toFlowable(BackpressureStrategy.BUFFER);
        return publisher;
    }

    Map<String, CIShellCIBridgeData> getCIBridgeDataMap() {
        return cibridgeDataMap;
    }

    Map<org.cishell.framework.data.Data, CIShellCIBridgeData> getCIShellDataCIBridgeDataMap() {
        return cishellDataCIBridgeDataMap;
    }
}
