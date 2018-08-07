package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.LocalCIShellContext;
import org.cishell.cibridge.core.model.*;
import org.cishell.cibridge.core.wrapper.ProgressTrackableAlgorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import java.util.*;

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {
    private CIShellCIBridge cibridge;
    private final HashMap<String, Long> algorithmInstanceCount = new HashMap<>();

    public void setCIBridge(CIShellCIBridge cibridge) {
        this.cibridge = cibridge;
    }

    @Override
    public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {

        List<AlgorithmDefinition> algorithmDefinitions = new ArrayList<>();
        PageInfo pageInfo = new PageInfo(false, false);
        AlgorithmDefinitionQueryResults queryResults = null;
        int limit = 0, offset = 0;

        for (Map.Entry<String, AlgorithmFactoryDataObject> entry : cibridge.algorithmFactoryDataMap.entrySet()) {
            algorithmDefinitions.add(entry.getValue().getAlgorithmDefinition());
        }

        if (filter == null) {
            System.out.println("Filter is empty!");
            queryResults = new AlgorithmDefinitionQueryResults(algorithmDefinitions, pageInfo);
        } else {

            try {

                //filter based on algorithm definition ids
                if (filter.getAlgorithmDefinitionIds() != null) {
                    algorithmDefinitions.removeIf(algorithmDefinition -> !filter.getAlgorithmDefinitionIds().contains(algorithmDefinition.getId()));
                }

                //filter based on algorithm instance ids
                if (filter.getAlgorithmInstanceIds() != null) {
                    Set<String> algorithmDefinitionIds = new HashSet<>();

                    for (String instanceId : filter.getAlgorithmInstanceIds()) {
                        if (cibridge.algorithmDataMap.get(instanceId) != null) {
                            algorithmDefinitionIds.add(cibridge.algorithmDataMap.get(instanceId).getAlgorithmInstance().getAlgorithmDefinition().getId());
                        }
                    }

                    algorithmDefinitions.removeIf(algorithmDefinition -> !algorithmDefinitionIds.contains(algorithmDefinition.getId()));
                }

                //filter based on input data format
                if (filter.getInputFormats() != null) {
                    algorithmDefinitions.removeIf(algorithmDefinition -> Collections.disjoint(filter.getInputFormats(), algorithmDefinition.getInData()));
                }

                //filter based on output data format
                if (filter.getOutputFormats() != null) {
                    algorithmDefinitions.removeIf(algorithmDefinition -> Collections.disjoint(filter.getOutputFormats(), algorithmDefinition.getOutData()));
                }

                //filter based on input data ids
                // TODO right now, nothing is being stored in memory for data instances. Uncomment this after the caching data instances
                /*if (filter.getInputDataIds() != null) {
                    Set<String> supportedFormats = new HashSet<>();
                    for (String inputDataId : filter.getInputDataIds()) {
                        supportedFormats.add(cibridge.dataInstanceMap.get(inputDataId).getFormat());
                    }
                    algorithmDefinitions.removeIf(algorithmDefinition -> Collections.disjoint(algorithmDefinition.getInData(), supportedFormats));
                }*/

                limit = filter.getLimit();
                offset = filter.getOffset();
                queryResults = new AlgorithmDefinitionQueryResults(algorithmDefinitions, pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
                queryResults = new AlgorithmDefinitionQueryResults(new ArrayList<>(), pageInfo);
            }

        }
        return (AlgorithmDefinitionQueryResults) cibridge.getPaginatedQueryResults(queryResults, limit, offset);

    }

    private AlgorithmDefinition setAlgorithmDefinition(ServiceReference ref) {

        AlgorithmDefinition algorithmDefinition = new AlgorithmDefinition(
                ref.getProperty(Constants.SERVICE_PID) != null ? ref.getProperty(Constants.SERVICE_PID).toString() : null,
                null,//InputParameters parameters
                ref.getProperty(AlgorithmProperty.IN_DATA) != null ? Arrays.asList(ref.getProperty(AlgorithmProperty.IN_DATA).toString()) : null,//List<String> inData
                ref.getProperty(AlgorithmProperty.OUT_DATA) != null ? Arrays.asList(ref.getProperty(AlgorithmProperty.OUT_DATA).toString()) : null,//List<String> outData
                ref.getProperty(AlgorithmProperty.LABEL) != null ? ref.getProperty(AlgorithmProperty.LABEL).toString() : null,//String label
                ref.getProperty(AlgorithmProperty.DESCRIPTION) != null ? ref.getProperty(AlgorithmProperty.DESCRIPTION).toString() : null,//String description
                null,//Boolean parentOutputData
                ref.getProperty(AlgorithmProperty.ALGORITHM_TYPE) != null ? AlgorithmType.valueOf(ref.getProperty(AlgorithmProperty.ALGORITHM_TYPE).toString()) : null,//AlgorithmType type
                ref.getProperty(AlgorithmProperty.REMOTEABLE) != null ? Boolean.valueOf(ref.getProperty(AlgorithmProperty.REMOTEABLE).toString()) : null,//String menuPath,//Boolean remoteable
                ref.getProperty(AlgorithmProperty.MENU_PATH) != null ? ref.getProperty(AlgorithmProperty.MENU_PATH).toString() : null,//String menuPath
                ref.getProperty(AlgorithmProperty.CONVERSION) != null ? ConversionType.valueOf(ref.getProperty(AlgorithmProperty.CONVERSION).toString()) : null,//ConversionType conversion
                ref.getProperty(AlgorithmProperty.AUTHORS) != null ? ref.getProperty(AlgorithmProperty.AUTHORS).toString() : null,//String authors
                ref.getProperty(AlgorithmProperty.IMPLEMENTERS) != null ? ref.getProperty(AlgorithmProperty.IMPLEMENTERS).toString() : null,//String implementers
                ref.getProperty(AlgorithmProperty.INTEGRATORS) != null ? ref.getProperty(AlgorithmProperty.INTEGRATORS).toString() : null,//String integrators
                ref.getProperty(AlgorithmProperty.DOCUMENTATION_URL) != null ? ref.getProperty(AlgorithmProperty.DOCUMENTATION_URL).toString() : null,//String documentationUrl
                ref.getProperty(AlgorithmProperty.REFERENCE) != null ? ref.getProperty(AlgorithmProperty.REFERENCE).toString() : null,//String reference
                ref.getProperty(AlgorithmProperty.REFERENCE_URL) != null ? ref.getProperty(AlgorithmProperty.REFERENCE_URL).toString() : null,//String referenceUrl
                ref.getProperty(AlgorithmProperty.WRITTEN_IN) != null ? ref.getProperty(AlgorithmProperty.WRITTEN_IN).toString() : null,//String writtenIn
                null//List<Property> otherProperties
        );

        System.out.println("AlgorithmDefinition created: " + algorithmDefinition);

        return algorithmDefinition;
    }

    @Override
    public AlgorithmInstanceQueryResults getAlgorithmInstances(AlgorithmFilter filter) {

        List<AlgorithmInstance> algorithmInstances = new ArrayList<>();
        PageInfo pageInfo = new PageInfo(false, false);
        AlgorithmInstanceQueryResults queryResults = null;
        int limit = 0, offset = 0;

        for (Map.Entry<String, AlgorithmDataObject> entry : cibridge.algorithmDataMap.entrySet()) {
            algorithmInstances.add(entry.getValue().getAlgorithmInstance());
        }

        if (filter == null) {
            System.out.println("Filter is empty!");
            queryResults = new AlgorithmInstanceQueryResults(algorithmInstances, pageInfo);
        } else {

            try {
                //filter based on algorithm definition ids
                if (filter.getAlgorithmDefinitionIds() != null) {
                    algorithmInstances.removeIf(algorithmInstance -> !filter.getAlgorithmDefinitionIds().contains(algorithmInstance.getAlgorithmDefinition().getId()));
                }

                //filter based on algorithm instance ids
                if (filter.getAlgorithmInstanceIds() != null) {
                    algorithmInstances.removeIf(algorithmInstance -> !filter.getAlgorithmInstanceIds().contains(algorithmInstance.getId()));
                }

                //filter based on input data ids
                if (filter.getInputDataIds() != null) {
                    algorithmInstances.removeIf(algorithmInstance -> Collections.disjoint(filter.getInputDataIds(), algorithmInstance.getInData()));
                }

                //filter based on algorithm instance states
                if (filter.getStates() != null) {
                    algorithmInstances.removeIf(algorithmInstance -> !filter.getStates().contains(algorithmInstance.getState()));
                }

                //filter based on teh input data format
                if (filter.getInputFormats() != null) {
                    algorithmInstances.removeIf(algorithmInstance -> Collections.disjoint(filter.getInputFormats(), algorithmInstance.getAlgorithmDefinition().getInData()));
                }

                //filter based on the output data format
                if (filter.getOutputFormats() != null) {
                    algorithmInstances.removeIf(algorithmInstance -> Collections.disjoint(filter.getOutputFormats(), algorithmInstance.getAlgorithmDefinition().getOutData()));
                }

                limit = filter.getLimit();
                offset = filter.getOffset();
                queryResults = new AlgorithmInstanceQueryResults(algorithmInstances, pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
                queryResults = new AlgorithmInstanceQueryResults(new ArrayList<>(), pageInfo);
            }
        }

        return (AlgorithmInstanceQueryResults) cibridge.getPaginatedQueryResults(queryResults, limit, offset);
    }

    @Override
    public AlgorithmInstance createAlgorithm(String algorithmDefinitionId, List<String> dataIds,
                                             List<PropertyInput> parameters) {

        AlgorithmDefinition algorithmDefinition = cibridge.algorithmFactoryDataMap.get(algorithmDefinitionId).getAlgorithmDefinition();
        AlgorithmFactory algorithmFactory = cibridge.algorithmFactoryDataMap.get(algorithmDefinitionId).getAlgorithmFactory();

        List<Data> dataList = new ArrayList<>();
        List<Property> paramList = new ArrayList<>();

        if (dataIds != null) {
            DataQueryResults dataQueryResults = cibridge.cishellData.getData(new DataFilter(dataIds, null, null, null, null));
            dataList = dataQueryResults != null ? dataQueryResults.getResults() : null;
        }

        org.cishell.framework.data.Data[] dataArray = null;
        if (dataList != null && !dataList.isEmpty()) {
            dataArray = (org.cishell.framework.data.Data[]) dataList.toArray();
        }

        Dictionary<String, Object> paramTable = new Hashtable<>();
        if (parameters != null) {
            for (PropertyInput property : parameters) {
                paramList.add(new Property(property.getKey(), property.getValue()));
                paramTable.put(property.getKey(), property.getValue());
            }
        }

        ProgressTrackableAlgorithm algorithm = (ProgressTrackableAlgorithm) algorithmFactory.createAlgorithm(dataArray, paramTable, new LocalCIShellContext(cibridge.getBundleContext()));
        algorithm.setProgressMonitor(ProgressMonitor.NULL_MONITOR);

        String algorithmInstanceId = generateAndGetInstanceId(algorithmDefinitionId);
        AlgorithmInstance algorithmInstance = new AlgorithmInstance(algorithmInstanceId, dataList, paramList, algorithmDefinition, AlgorithmState.IDLE, null, 1, null);

        AlgorithmDataObject algorithmData = new AlgorithmDataObject(algorithm, algorithmInstance);
        cibridge.algorithmDataMap.put(algorithmInstanceId, algorithmData);

        System.out.println("Algorithm Instance created: " + algorithmInstance);

        return algorithmInstance;
    }

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

    private String generateAndGetInstanceId(String algorithmDefinitionId) {
        Long newCount;

        if (algorithmInstanceCount.get(algorithmDefinitionId) != null) {
            newCount = algorithmInstanceCount.get(algorithmDefinitionId) + 1;
        } else {
            newCount = 0L;
        }

        algorithmInstanceCount.put(algorithmDefinitionId, newCount);
        return algorithmDefinitionId + newCount;

    }

    public void cacheData() {
        try {
            ServiceReference[] refs = cibridge.getBundleContext().getServiceReferences(AlgorithmFactory.class.getName(), null);
            System.out.println("Caching AlgorithmDefinitions...");

            if (refs != null) {
                for (ServiceReference ref : refs) {
                    if (ref.getProperty(Constants.SERVICE_PID) != null) {
                        String pid = ref.getProperty(Constants.SERVICE_PID).toString();
                        if (pid != null) {
                            System.out.println("Caching Service pid: " + pid);
                            AlgorithmDefinition algorithmDefinition = setAlgorithmDefinition(ref);
                            AlgorithmFactory algorithmFactory = cibridge.getAlgorithmFactory(pid);

                            AlgorithmFactoryDataObject algorithmFactoryData = new AlgorithmFactoryDataObject(algorithmDefinition, algorithmFactory);
                            cibridge.algorithmFactoryDataMap.put(pid, algorithmFactoryData);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
