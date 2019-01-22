package org.cishell.cibridge.cishell.impl;

import com.google.common.base.Preconditions;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.osgi.framework.Constants.OBJECTCLASS;

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {
    private CIShellCIBridge cibridge;
    //todo should we make this concurrent hashmap?
    private final Map<String, CIShellCIBridgeAlgorithmInstance> algorithmInstanceCache = new LinkedHashMap<>();
    private final Map<String, CIShellCIBridgeAlgorithmDefinition> algorithmDefinitionCache = new LinkedHashMap<>();

    public void setCIBridge(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
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
                if (algorithmInstanceCache.get(instanceId) != null) {
                    algorithmDefinitionIds.add(algorithmInstanceCache.get(instanceId).getAlgorithmDefinition().getId());
                }
            }

            criteria.add(algorithmDefinition -> {
                if (algorithmDefinition == null) return false;
                return algorithmDefinitionIds.contains(algorithmDefinition.getId());
            });
        }

        //predicate on input data ids
        //todo need to clarify about how to filter based on input data ids
        if (filter.getInputDataIds() != null) {
            Set<String> supportedFormats = new HashSet<>();
            for (String inputDataId : filter.getInputDataIds()) {
                supportedFormats.add(cibridge.cishellData.getDataCache().get(inputDataId).getFormat());
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

        //todo how to filter algorithm that have no input or output data format
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
                new ArrayList<>(algorithmDefinitionCache.values()), criteria, filter.getOffset(), filter.getLimit());

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
        // filter based on input data format
        // filter based on output data format

        QueryResults<AlgorithmInstance> paginatedQueryResults = PaginationUtil.getPaginatedResults(
                new ArrayList<>(algorithmInstanceCache.values()), criteria, filter.getOffset(), filter.getLimit());

        return new AlgorithmInstanceQueryResults(paginatedQueryResults.getResults(), paginatedQueryResults.getPageInfo());
    }

    @Override
    public AlgorithmInstance createAlgorithm(String algorithmDefinitionId, List<String> dataIds, List<PropertyInput> parameters) {
        CIShellCIBridgeAlgorithmDefinition algorithmDefinition = algorithmDefinitionCache.get(algorithmDefinitionId);
        AlgorithmFactory algorithmFactory = algorithmDefinitionCache.get(algorithmDefinitionId).getAlgorithmFactory();

        List<CIShellCIBridgeData> dataList = new ArrayList<>();
        List<Property> paramList = new ArrayList<>();

        if (dataIds != null && !dataIds.isEmpty()) {
            dataList = cibridge.cishellData.getDataCache().entrySet()
                    .stream()
                    .filter(entry -> dataIds.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }

        org.cishell.framework.data.Data[] dataArray = null;
        if (!dataList.isEmpty()) {
            dataArray = (org.cishell.framework.data.Data[]) dataList.stream().map(CIShellCIBridgeData::getCIShellData).toArray();
        }

        Dictionary<String, Object> paramTable = null;
        if (parameters != null && !parameters.isEmpty()) {
            paramTable = new Hashtable<>();
            for (PropertyInput property : parameters) {
                paramList.add(new Property(property.getKey(), property.getValue()));
                paramTable.put(property.getKey(), property.getValue());
            }
        }

        Algorithm algorithm = algorithmFactory.createAlgorithm(dataArray, paramTable, cibridge.getCIShellContext());

        CIShellCIBridgeAlgorithmInstance algorithmInstance = new CIShellCIBridgeAlgorithmInstance(algorithmDefinition, algorithm);
        algorithmInstance.setParameters(paramList);

        algorithmInstanceCache.put(algorithmInstance.getId(), algorithmInstance);
        return algorithmInstance;
    }

    //TODO Implement subscription methods below
    @Override
    public AlgorithmDefinition algorithmDefinitionAdded() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AlgorithmDefinition algorithmDefinitionRemoved() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AlgorithmInstance algorithmInstanceUpdated(AlgorithmFilter filter) {
        // TODO Auto-generated method stub
        return null;
    }

    private void uncacheAlgorithmDefinition(ServiceReference<AlgorithmFactory> reference) {
        if (reference.getProperty(Constants.SERVICE_PID) != null) {
            String pid = reference.getProperty(Constants.SERVICE_PID).toString();
            if (pid != null) {
                algorithmDefinitionCache.remove(pid);
            }
        }
    }

    private void cacheAlgorithmDefinition(ServiceReference<AlgorithmFactory> reference) {
        if (reference != null && reference.getProperty(Constants.SERVICE_PID) != null) {
            String pid = reference.getProperty(Constants.SERVICE_PID).toString();
            if (pid != null) {
                CIShellCIBridgeAlgorithmDefinition algorithmDefinition = new CIShellCIBridgeAlgorithmDefinition(reference, cibridge.getBundleContext().getService(reference));
                algorithmDefinitionCache.put(pid, algorithmDefinition);
            }
        }
    }

    public void cacheAlgorithmDefinitions() {
        //cache all the algorithms registered in the future through service listener
        try {
            cibridge.getBundleContext().addServiceListener(event -> {
                if (event.getType() == ServiceEvent.REGISTERED) {
                    cacheAlgorithmDefinition((ServiceReference<AlgorithmFactory>) event.getServiceReference());
                } else if (event.getType() == ServiceEvent.UNREGISTERING) {
                    uncacheAlgorithmDefinition((ServiceReference<AlgorithmFactory>) event.getServiceReference());
                } else if (event.getType() == ServiceEvent.MODIFIED) {
                    //TODOda
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

    Map<String, CIShellCIBridgeAlgorithmInstance> getAlgorithmInstanceCache() {
        return algorithmInstanceCache;
    }

    Map<String, CIShellCIBridgeAlgorithmDefinition> getAlgorithmDefinitionCache() {
        return algorithmDefinitionCache;
    }

}
