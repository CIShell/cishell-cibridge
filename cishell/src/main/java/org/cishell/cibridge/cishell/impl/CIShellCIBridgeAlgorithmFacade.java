package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;
import org.cishell.cibridge.core.model.interfaces.QueryResults;
import org.cishell.cibridge.core.wrapper.ProgressTrackableAlgorithm;

import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.osgi.framework.BundleContext;
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
        BundleContext context = cibridge.getBundleContext();
        PageInfo pageInfo = new PageInfo(false, false);
        AlgorithmDefinitionQueryResults queryResults = null;
        HashMap<String, AlgorithmDefinition> algorithmDefinitionMap = new HashMap<>();
        int limit = 0, offset = 0;
        try {
            if (filter != null) {
                System.out.println("Filter :" + filter);
                System.out.println(filter);
                if (filter.getAlgorithmDefinitionIds() != null) {
                    for (String pids : filter.getAlgorithmDefinitionIds()) {
                        ServiceReference[] refs = context.getServiceReferences(AlgorithmFactory.class.getName(),
                                "(&(" + Constants.SERVICE_PID + "=" + pids + "))");
                        if (refs != null) {
                            for (ServiceReference ref : refs) {
                                algorithmDefinitions.add(setAlgorithmDefinition(ref));
                                //algorithmDefinitionMap.put(pids,setAlgorithmDefinition(ref));
                            }

                        }
                    }
                }

                if (filter.getAlgorithmInstanceIds() != null) {
                    for (String instanceId : filter.getAlgorithmInstanceIds()) {
                        if (cibridge.algorithmDataMap.get(instanceId) != null && cibridge.algorithmDataMap.get(instanceId).getAlgorithmInstance().getAlgorithmDefinition() != null) {
                            algorithmDefinitionMap.put(cibridge.algorithmDataMap.get(instanceId).getAlgorithmInstance().getAlgorithmDefinition().getId(),
                                    cibridge.algorithmDataMap.get(instanceId).getAlgorithmInstance().getAlgorithmDefinition());
                        }
                    }
                }

                if (filter.getInputDataIds() != null) {
                    for (String inputIds : filter.getInputDataIds()) {
                        //algorithmDefinitions.add()
                    }
                }

                if (filter.getInputFormats() != null) {
                    for (String inpformats : filter.getInputFormats()) {
                        //algorithmDefinitions.add()
                    }
                }
                if (filter.getOutputFormats() != null) {
                    for (String outformats : filter.getOutputFormats()) {
                        //algorithmDefinitions.add()
                    }
                }

                if (filter.getStates() != null) {
                    for (AlgorithmState state : filter.getStates()) {
                        //algorithmDefinitions.add()
                    }
                }

                limit = filter.getLimit();
                offset = filter.getOffset();
            } else {
                System.out.println("Filter is empty!");
                ServiceReference[] refs = context.getServiceReferences(AlgorithmFactory.class.getName(), null);
                for (ServiceReference ref : refs) {
                    algorithmDefinitions.add(setAlgorithmDefinition(ref));
                }
            }

            queryResults = new AlgorithmDefinitionQueryResults(algorithmDefinitions, pageInfo);
            return (AlgorithmDefinitionQueryResults) getQueryResults(queryResults, limit, offset);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return queryResults;
    }

    public <T> QueryResults<T> getQueryResults(QueryResults<T> queryResults, int limit, int offset) {

        int listSize = queryResults.getResults().size();
        Boolean hasNext = false;
        Boolean hasPrevious = false;

        List<T> queryList;
        if (offset > 0) {
            if (offset >= listSize) {
                queryList = queryResults.getResults().subList(0, 0);
            } else if (limit > 0) {
                queryList = queryResults.getResults().subList(offset, Math.min(offset + limit, listSize));
                hasPrevious = true;
                if ((offset + limit) < listSize) {
                    hasNext = true;
                }
            } else {
                queryList = queryResults.getResults().subList(offset, listSize);
                hasPrevious = true;
            }
        } else if (limit > 0) {
            queryList = queryResults.getResults().subList(0, Math.min(limit, listSize));
            if (limit < listSize) {
                hasNext = true;
            }
        } else {
            queryList = queryResults.getResults().subList(0, listSize);
        }

        PageInfo pageInfo = new PageInfo(hasNext, hasPrevious);

        return queryResults.getQueryResults(queryList, pageInfo);
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
        // TODO Auto-generated method stub
        return null;
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

        ProgressTrackableAlgorithm algorithm = (ProgressTrackableAlgorithm) algorithmFactory.createAlgorithm(dataArray, paramTable, null);
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
