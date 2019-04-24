package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.internal.subscribers.BlockingBaseSubscriber;
import io.reactivex.observables.ConnectableObservable;
import org.apache.commons.io.FilenameUtils;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeAlgorithmDefinition;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeData;
import org.cishell.cibridge.cishell.model.CIShellData;
import org.cishell.cibridge.cishell.util.PaginationUtil;
import org.cishell.cibridge.cishell.util.Util;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.service.conversion.Converter;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;
import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.cishell.framework.data.DataProperty.*;

public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {

    private CIShellCIBridge cibridge;
    private DataManagerListenerImpl dataManagerListener = new DataManagerListenerImpl();
    private final Map<String, CIShellCIBridgeData> cibridgeDataMap = new LinkedHashMap<>();
    private final Map<org.cishell.framework.data.Data, CIShellCIBridgeData> cishellDataCIBridgeDataMap = new HashMap<>();
    private ConnectableObservable<org.cishell.cibridge.core.model.Data> dataAddedObservable;
    private ObservableEmitter<org.cishell.cibridge.core.model.Data> dataAddedObservableEmitter;
    private ConnectableObservable<org.cishell.cibridge.core.model.Data> dataRemovedObservable;
    private ObservableEmitter<org.cishell.cibridge.core.model.Data> dataRemovedObservableEmitter;
    private ConnectableObservable<Data> dataUpdatedObservable;
    private ObservableEmitter<Data> dataUpdatedObservableEmitter;

    public void setCIBridge(CIShellCIBridge ciBridge) {
        Preconditions.checkNotNull(ciBridge, "CIBridge cannot be null");
        this.cibridge = ciBridge;
        this.dataManagerListener.setCIBridge(ciBridge);
    }

    public CIShellCIBridgeDataFacade() {
        Observable<Data> dataupdatedobservable = Observable.create(emitter -> {
            dataUpdatedObservableEmitter = emitter;

        });
        dataUpdatedObservable = dataupdatedobservable.share().publish();
        dataUpdatedObservable.connect();

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

    @Override
    public String validateData(String algorithmDefinitionId, List<String> dataIds) {
        return null;
    }

    @Override
    public List<AlgorithmDefinition> findConverters(String dataId, String outFormat) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkNotNull(outFormat, "output format cannot be null");
        Preconditions.checkArgument(cibridgeDataMap.containsKey(dataId), "No data found with id '%s'", dataId);
        CIShellCIBridgeData cibridgeData = cibridgeDataMap.get(dataId);
        Converter[] converters = cibridge.getDataConversionService().findConverters(cibridgeData.getCIShellData(), outFormat);
        return getAlgorithmDefinitionListFromConverters(converters);
    }

    @Override
    public List<AlgorithmDefinition> findConvertersByFormat(String inFormat, String outFormat) {
        Preconditions.checkNotNull(inFormat, "input format cannot be null");
        Preconditions.checkNotNull(outFormat, "output format cannot be null");
        Converter[] converters = cibridge.getDataConversionService().findConverters(inFormat, outFormat);
        return getAlgorithmDefinitionListFromConverters(converters);
    }

    private List<AlgorithmDefinition> getAlgorithmDefinitionListFromConverters(Converter[] converters) {
        List<AlgorithmDefinition> algorithmDefinitionList = new ArrayList<>();

        for (Converter converter : converters) {
            CIShellCIBridgeAlgorithmDefinition algorithmDefinition = new CIShellCIBridgeAlgorithmDefinition(converter);
            cibridge.cishellAlgorithm.getAlgorithmDefinitionMap().put(algorithmDefinition.getId(), algorithmDefinition);
            cibridge.cishellAlgorithm.getAlgorithmDefinitionAddedObservableEmitter().onNext(algorithmDefinition);
            algorithmDefinitionList.add(algorithmDefinition);
        }

        return algorithmDefinitionList;
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
                new ArrayList<>(cibridgeDataMap.values()), criteria, filter.getOffset(), filter.getLimit());
        return new DataQueryResults(paginatedQueryResults.getResults(), paginatedQueryResults.getPageInfo());
    }

    @Override
    public String downloadData(String dataId) {
        Preconditions.checkNotNull(dataId, "dataId cannot be null");
        Preconditions.checkArgument(cibridgeDataMap.containsKey(dataId), "Invalid dataId. No data found with id '%s'", dataId);
        return cibridgeDataMap.get(dataId).getCIShellData().getData().toString();
    }

    /* Mutations */
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
            format = "file-ext:" + FilenameUtils.getExtension(filePath);
        }

        if (format.startsWith("file-ext:")) {
            return validateAndLoadData(filePath, format, properties);
        } else {
            return loadData(file, format, properties);
        }
    }

    private Data validateAndLoadData(String filePath, String format, DataProperties properties) {

        List<AlgorithmDefinition> validatorAlgorithms = getValidatorAlgorithms(format);

        AlgorithmDefinition validatorAlgorithm = null;

        Preconditions.checkArgument(validatorAlgorithms.size() > 0, "No validator algorithm found for the data");
        if (validatorAlgorithms.size() > 1) {
            GUIBuilderService guiBuilderService = cibridge.getGUIBuilderService();
            String pid = "";

            MetaTypeProvider metaTypeProvider = new MetaTypeProvider() {
                @Override
                public ObjectClassDefinition getObjectClassDefinition(String s, String s1) {
                    return new SelectValidatorAlgorithmOCD();
                }

                @Override
                public String[] getLocales() {
                    return new String[0];
                }
            };

            Dictionary userEnteredParameters = guiBuilderService.createGUIandWait(pid, metaTypeProvider);
            String validatorAlgorithmPid = userEnteredParameters.get("userEnteredValidatorAlgorithm").toString();
            validatorAlgorithm = cibridge.cishellAlgorithm.getAlgorithmDefinitionMap().get(validatorAlgorithmPid);
        } else {
            validatorAlgorithm = validatorAlgorithms.get(0);
        }

        //upload unvalidated data
        Data unvalidatedData = loadData(filePath, format, properties);

        AlgorithmInstance algorithmInstance = cibridge.cishellAlgorithm.createAlgorithm(validatorAlgorithm.getId(), Collections.singletonList(unvalidatedData.getId()), null);

        AlgorithmFilter algorithmFilter = new AlgorithmFilter();
        algorithmFilter.setAlgorithmInstanceIds(Collections.singletonList(algorithmInstance.getId()));
        algorithmFilter.setStates(Collections.singletonList(AlgorithmState.FINISHED));

        final Object algorithmMonitor = new Object();
        cibridge.cishellAlgorithm.algorithmInstanceUpdated(algorithmFilter).subscribe(new AlgorithmSubscriber(algorithmMonitor));

        cibridge.cishellScheduler.runAlgorithmNow(algorithmInstance.getId());

        try {
            synchronized (algorithmMonitor) {
                algorithmMonitor.wait(20000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (algorithmInstance.getOutData() != null && algorithmInstance.getOutData().size() > 0) {
            return algorithmInstance.getOutData().get(0);
        }

        return null;
    }

    private class SelectValidatorAlgorithmOCD implements ObjectClassDefinition {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getID() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public AttributeDefinition[] getAttributeDefinitions(int i) {
            return new AttributeDefinition[0];
        }

        @Override
        public InputStream getIcon(int i) throws IOException {
            return null;
        }
    }

    private class AlgorithmSubscriber extends BlockingBaseSubscriber<AlgorithmInstance> {

        private final Object algorithmMonitor;

        private AlgorithmSubscriber(Object algorithmMonitor) {
            this.algorithmMonitor = algorithmMonitor;
        }

        @Override
        public void onNext(AlgorithmInstance algorithmInstance) {
            synchronized (algorithmMonitor) {
                //algorithm finished
                algorithmMonitor.notify();
            }
        }

        @Override
        public void onError(Throwable t) {

        }
    }

    private Data loadData(Object data, String format, DataProperties properties) {
        //create CIShellData object which is an implementation of CIShell frameworks's Data interface
        CIShellData cishellData = new CIShellData(data, format);

        if (properties != null) {
            updateCIShellDataProperties(cishellData, properties);
        }
        //add the CIShellData object wrapped inside CIShellCIBridgeData to data manager service
        cibridge.getDataManagerService().addData(cishellData);
        return cishellDataCIBridgeDataMap.get(cishellData);
    }

    private List<AlgorithmDefinition> getValidatorAlgorithms(String format) {
        List<AlgorithmDefinition> validatorAlgorithms = new ArrayList<>();
        for (AlgorithmDefinition algorithmDefinition : cibridge.cishellAlgorithm.getAlgorithmDefinitionMap().values()) {
            if (algorithmDefinition.getType().equals(AlgorithmType.VALIDATOR)) {
                if (algorithmDefinition.getInData().size() > 0) {
                    String indataFormat = algorithmDefinition.getInData().get(0);
                    if (indataFormat.equals(format)) {
                        validatorAlgorithms.add(algorithmDefinition);
                    }
                }
            }
        }
        return validatorAlgorithms;
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
        Preconditions.checkArgument(cibridgeDataMap.containsKey(dataId), "Invalid dataId. No data found with id '%s'", dataId);
        CIShellCIBridgeData cishellCIBridgedata = cibridgeDataMap.get(dataId);
        //update CIShellCIBridgeData properties
        updateCIShellCIBridgeDataProperties(cishellCIBridgedata, properties);
        //also update properties of CIShellData object wrapped inside CIShellCIBridgeData object
        updateCIShellDataProperties(cishellCIBridgedata.getCIShellData(), properties);
        dataUpdatedObservableEmitter.onNext(cishellCIBridgedata);
        return true;
    }

    private void updateCIShellCIBridgeDataProperties(Data data, DataProperties properties) {
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
        return Util.asPublisher(dataAddedObservable);
    }

    @Override
    public Publisher<Data> dataRemoved() {
        return Util.asPublisher(dataRemovedObservable);
    }

    @Override
    public Publisher<Data> dataUpdated() {
        return Util.asPublisher(dataUpdatedObservable);
    }

    Map<String, CIShellCIBridgeData> getCIBridgeDataMap() {
        return cibridgeDataMap;
    }

    Map<org.cishell.framework.data.Data, CIShellCIBridgeData> getCIShellDataCIBridgeDataMap() {
        return cishellDataCIBridgeDataMap;
    }

    DataManagerListenerImpl getDataManagerListener() {
        return dataManagerListener;
    }

    protected ObservableEmitter<Data> getDataAddedObservableEmitter() {
        return dataAddedObservableEmitter;
    }

    protected ObservableEmitter<Data> getDataRemovedObservableEmitter() {
        return dataRemovedObservableEmitter;
    }

    protected ObservableEmitter<Data> getDataUpdatedObservableEmitter() {
        return dataUpdatedObservableEmitter;
    }
}
