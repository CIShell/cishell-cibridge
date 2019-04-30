package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeAlgorithmDefinition;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeAlgorithmInstance;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeData;
import org.cishell.cibridge.cishell.util.PaginationUtil;
import org.cishell.cibridge.cishell.util.Util;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.cishell.cibridge.cishell.util.Util.getParsedValue;
import static org.osgi.framework.Constants.OBJECTCLASS;
import static org.osgi.framework.Constants.SERVICE_PID;

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {

    private CIShellCIBridge cibridge;
    private final Map<String, CIShellCIBridgeAlgorithmInstance> algorithmInstanceMap = new LinkedHashMap<>();
    private final Map<Algorithm, CIShellCIBridgeAlgorithmInstance> cishellAlgorithmCIBridgeAlgorithmMap = new HashMap<>();
    private final Map<String, CIShellCIBridgeAlgorithmDefinition> algorithmDefinitionMap = new LinkedHashMap<>();
    private ConnectableObservable<AlgorithmDefinition> algorithmDefinitionAddedObservable;
    private ObservableEmitter<AlgorithmDefinition> algorithmDefinitionAddedObservableEmitter;

    private ConnectableObservable<AlgorithmDefinition> algorithmDefinitionRemovedObservable;
    private ObservableEmitter<AlgorithmDefinition> algorithmDefinitionRemovedObservableEmitter;

    private ConnectableObservable<AlgorithmInstance> algorithmInstanceUpdatedObservable;
    private ObservableEmitter<AlgorithmInstance> algorithmInstanceUpdatedObservableEmitter;

    public CIShellCIBridgeAlgorithmFacade() {
        io.reactivex.Observable<AlgorithmDefinition> algorithmaddedobservable = Observable.create(emitter -> {
            algorithmDefinitionAddedObservableEmitter = emitter;

        });
        algorithmDefinitionAddedObservable = algorithmaddedobservable.share().publish();
        algorithmDefinitionAddedObservable.connect();

        io.reactivex.Observable<AlgorithmDefinition> algorithmremovedobservable = Observable.create(emitter -> {
            algorithmDefinitionRemovedObservableEmitter = emitter;

        });
        algorithmDefinitionRemovedObservable = algorithmremovedobservable.share().publish();
        algorithmDefinitionRemovedObservable.connect();

        io.reactivex.Observable<AlgorithmInstance> algorithmupdatedobservable = Observable.create(emitter -> {
            algorithmInstanceUpdatedObservableEmitter = emitter;

        });
        algorithmInstanceUpdatedObservable = algorithmupdatedobservable.share().publish();
        algorithmInstanceUpdatedObservable.connect();
    }

    public void setCIBridge(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
        cacheAlgorithmDefinitions();
    }

    @Override
    public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {
        Preconditions.checkNotNull(filter, "filter cannot be null");
        List<Predicate<AlgorithmDefinition>> criteria = new LinkedList<>();
        //predicate on algorithm definitions
        if (filter.getAlgorithmDefinitionIds() != null) {
            criteria.add(algorithmDefinition -> {
                if (algorithmDefinition == null) return false;
                return filter.getAlgorithmDefinitionIds().contains(algorithmDefinition.getId());
            });
        }

        //predicate on algorithm instances
        if (filter.getAlgorithmInstanceIds() != null) {
            Set<String> algorithmDefinitionIds = new HashSet<>();
            for (String instanceId : filter.getAlgorithmInstanceIds()) {
                if (algorithmInstanceMap.get(instanceId) != null) {
                    algorithmDefinitionIds.add(algorithmInstanceMap.get(instanceId).getAlgorithmDefinition().getId());
                }
            }
            criteria.add(algorithmDefinition -> {
                if (algorithmDefinition == null) return false;
                return algorithmDefinitionIds.contains(algorithmDefinition.getId());
            });
        }
        //predicate on input data ids
        if (filter.getInputDataIds() != null) {
            Set<String> supportedFormats = new HashSet<>();
            for (String inputDataId : filter.getInputDataIds()) {
                supportedFormats.add(cibridge.cishellData.getCIBridgeDataMap().get(inputDataId).getFormat());
            }
            criteria.add(algorithmDefinition -> {
                if (algorithmDefinition == null) return false;
                return !Collections.disjoint(algorithmDefinition.getInData(), supportedFormats);
            });
        }
        //predicate on input data format
        if (filter.getInputFormats() != null) {
            criteria.add(algorithmDefinition -> {
                if (algorithmDefinition == null) return false;

                return !Collections.disjoint(algorithmDefinition.getInData(), filter.getInputFormats());
            });
        }
        //predicate on output data format
        if (filter.getOutputFormats() != null) {
            criteria.add(algorithmDefinition -> {
                if (algorithmDefinition == null) return false;

                return !Collections.disjoint(algorithmDefinition.getOutData(), filter.getOutputFormats());
            });
        }
        //predicate on other properties
        if (filter.getProperties() != null) {
            Map<String, Set<String>> propertyValuesMap = new HashMap<>();
            filter.getProperties().forEach(propertyInput -> {
                if (!propertyValuesMap.containsKey(propertyInput.getKey())) {
                    propertyValuesMap.put(propertyInput.getKey(), new HashSet<>());
                }
                propertyValuesMap.get(propertyInput.getKey()).add(propertyInput.getValue());
            });
            for (Map.Entry<String, Set<String>> entry : propertyValuesMap.entrySet()) {
                criteria.add(algorithmDefinition -> {
                    String key = entry.getKey();

                    if (algorithmDefinition == null) return false;
                    Map<String, String> otherProperties = algorithmDefinition.getOtherProperties().stream().collect(Collectors.toMap(Property::getKey, Property::getValue));
                    if (otherProperties.containsKey(key)) {
                        return entry.getValue().contains(otherProperties.get(key));
                    }
                    return false;
                });
            }

        }
        QueryResults<AlgorithmDefinition> paginatedQueryResults = PaginationUtil.getPaginatedResults(
                new ArrayList<>(algorithmDefinitionMap.values()), criteria, filter.getOffset(), filter.getLimit());
        return new AlgorithmDefinitionQueryResults(paginatedQueryResults.getResults(), paginatedQueryResults.getPageInfo());
    }

    @Override
    public AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter) {
        Preconditions.checkNotNull(filter, "filter cannot be null");
        List<Predicate<AlgorithmInstance>> criteria = new LinkedList<>();
        //filter based on algorithm instance ids
        if (filter.getAlgorithmInstanceIds() != null) {
            criteria.add(algorithmInstance -> {
                if (algorithmInstance == null) return false;
                return filter.getAlgorithmInstanceIds().contains(algorithmInstance.getId());
            });
        }
        //filter based on algorithm definition ids
        if (filter.getAlgorithmDefinitionIds() != null) {
            criteria.add(algorithmInstance -> {
                if (algorithmInstance == null) return false;

                return filter.getAlgorithmDefinitionIds().contains(algorithmInstance.getAlgorithmDefinition().getId());
            });
        }
        // filter based on algorithm instance states
        if (filter.getStates() != null) {
            criteria.add(algorithmInstance -> {
                if (algorithmInstance == null) return false;

                return filter.getStates().contains(algorithmInstance.getState());
            });
        }
        // filter based on input data ids
        if (filter.getInputDataIds() != null) {
            criteria.add(algorithmInstance -> {
                if (algorithmInstance == null) return false;

                return !Collections.disjoint(algorithmInstance.getInData().stream().map(Data::getId).collect(Collectors.toList()), filter.getInputDataIds());
            });
        }

        // filter based on input data format
        if (filter.getInputFormats() != null) {
            criteria.add(algorithmInstance -> {
                if (algorithmInstance == null) return false;

                return !Collections.disjoint(algorithmInstance.getAlgorithmDefinition().getInData(), filter.getInputFormats());
            });
        }

        // filter based on output data format
        if (filter.getOutputFormats() != null) {
            criteria.add(algorithmInstance -> {
                if (algorithmInstance == null) return false;

                return !Collections.disjoint(algorithmInstance.getAlgorithmDefinition().getOutData(), filter.getOutputFormats());
            });
        }

        QueryResults<AlgorithmInstance> paginatedQueryResults = PaginationUtil.getPaginatedResults(
                new ArrayList<>(algorithmInstanceMap.values()), criteria, filter.getOffset(), filter.getLimit());

        return new AlgorithmInstanceQueryResults(paginatedQueryResults.getResults(), paginatedQueryResults.getPageInfo());
    }

    @Override
    public AlgorithmInstance createAlgorithm(String
                                                     algorithmDefinitionId, List<String> dataIds, List<PropertyInput> parameters) {
        CIShellCIBridgeAlgorithmDefinition algorithmDefinition = algorithmDefinitionMap.get(algorithmDefinitionId);
        AlgorithmFactory algorithmFactory = algorithmDefinitionMap.get(algorithmDefinitionId).getAlgorithmFactory();
        List<CIShellCIBridgeData> dataList = new ArrayList<>();
        List<Property> paramList = new ArrayList<>();
        if (dataIds != null && !dataIds.isEmpty()) {

            dataList = cibridge.cishellData.getCIBridgeDataMap().entrySet()
                    .stream()
                    .filter(entry -> dataIds.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        org.cishell.framework.data.Data[] dataArray = null;
        if (!dataList.isEmpty()) {
            dataArray = dataList.stream().map(CIShellCIBridgeData::getCIShellData).toArray(org.cishell.framework.data.Data[]::new);
        }
        Dictionary<String, Object> paramTable = null;
        if (parameters != null && !parameters.isEmpty()) {
            paramTable = new Hashtable<>();
            for (PropertyInput property : parameters) {
                paramList.add(new Property(property.getKey(), property.getValue(), property.getAttributeType()));
                paramTable.put(property.getKey(), getParsedValue(property.getValue(), property.getAttributeType()));
            }
        }
        Algorithm algorithm = algorithmFactory.createAlgorithm(dataArray, paramTable, cibridge.getCIShellContext());
        //create cibridge's algorithm object(algorithmInstance) containing cishell's algorithm object
        CIShellCIBridgeAlgorithmInstance algorithmInstance = new CIShellCIBridgeAlgorithmInstance(algorithmDefinition, algorithm);
        algorithmInstance.setParameters(paramList);
        algorithmInstance.setInData(dataList.stream().map(datum -> (Data) datum).collect(Collectors.toList()));
        algorithmInstanceMap.put(algorithmInstance.getId(), algorithmInstance);
        cishellAlgorithmCIBridgeAlgorithmMap.put(algorithm, algorithmInstance);
        return algorithmInstance;
    }

    @Override
    public Publisher<AlgorithmDefinition> algorithmDefinitionAdded() {
        return Util.asPublisher(algorithmDefinitionAddedObservable);
    }

    @Override
    public Publisher<AlgorithmDefinition> algorithmDefinitionRemoved() {
        return Util.asPublisher(algorithmDefinitionRemovedObservable);
    }

    @Override
    public Publisher<AlgorithmInstance> algorithmInstanceUpdated(AlgorithmFilter filter) {
        Flowable<AlgorithmInstance> publisher;
        publisher = algorithmInstanceUpdatedObservable.toFlowable(BackpressureStrategy.BUFFER);

        if (filter != null) {
            publisher = publisher.filter(algorithmInstance -> {
                return ((filter.getAlgorithmInstanceIds() == null || filter.getAlgorithmInstanceIds().contains(algorithmInstance.getId()))
                        && (filter.getAlgorithmDefinitionIds() == null || filter.getAlgorithmDefinitionIds().contains(algorithmInstance.getAlgorithmDefinition().getId()))
                        && (filter.getStates() == null || filter.getStates().contains(algorithmInstance.getState())));
            });
        }
        return publisher;
    }


    private void uncacheAlgorithmDefinition(ServiceReference<AlgorithmFactory> reference) {
        try {
            if (reference.getProperty(SERVICE_PID) != null) {
                String pid = reference.getProperty(SERVICE_PID).toString();

                if (pid != null) {
                    algorithmDefinitionMap.remove(pid);
                    algorithmDefinitionRemovedObservableEmitter.onNext(algorithmDefinitionMap.get(pid));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cacheAlgorithmDefinition(ServiceReference<AlgorithmFactory> reference) {
        try {
            if (reference != null && reference.getProperty(SERVICE_PID) != null) {
                String pid = reference.getProperty(SERVICE_PID).toString();
                if (pid != null) {
                    CIShellCIBridgeAlgorithmDefinition algorithmDefinition = new CIShellCIBridgeAlgorithmDefinition(cibridge, reference);
                    algorithmDefinitionMap.put(pid, algorithmDefinition);
                    algorithmDefinitionAddedObservableEmitter.onNext(algorithmDefinition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cacheAlgorithmDefinitions() {
        //cache all the algorithms registered in the future through service listener
        synchronized (this) {
            try {
                cibridge.getBundleContext().addServiceListener(event -> {
                    if (event.getType() == ServiceEvent.REGISTERED) {
                        cacheAlgorithmDefinition((ServiceReference<AlgorithmFactory>) event.getServiceReference());
                    } else if (event.getType() == ServiceEvent.UNREGISTERING) {
                        uncacheAlgorithmDefinition((ServiceReference<AlgorithmFactory>) event.getServiceReference());
                    }
                }, "(" + OBJECTCLASS + "=" + AlgorithmFactory.class.getName() + ")");
            } catch (InvalidSyntaxException ignored) {
            }

            //cache all the algorithms already registered
            try {
                Collection<ServiceReference<AlgorithmFactory>> references = cibridge.getBundleContext().getServiceReferences(AlgorithmFactory.class, null);

                if (references != null) {
                    for (ServiceReference<AlgorithmFactory> reference : references) {
                        cacheAlgorithmDefinition(reference);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected Map<String, CIShellCIBridgeAlgorithmInstance> getAlgorithmInstanceMap() {
        return algorithmInstanceMap;
    }

    protected Map<String, CIShellCIBridgeAlgorithmDefinition> getAlgorithmDefinitionMap() {
        return algorithmDefinitionMap;
    }

    protected Map<Algorithm, CIShellCIBridgeAlgorithmInstance> getCIShellAlgorithmCIBridgeAlgorithmMap() {
        return cishellAlgorithmCIBridgeAlgorithmMap;
    }

    protected ObservableEmitter<AlgorithmInstance> getAlgorithmInstanceUpdatedObservableEmitter() {
        return algorithmInstanceUpdatedObservableEmitter;
    }

    protected ObservableEmitter<AlgorithmDefinition> getAlgorithmDefinitionAddedObservableEmitter() {
        return algorithmDefinitionAddedObservableEmitter;
    }
}
