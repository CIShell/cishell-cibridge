package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.core.model.AlgorithmState;
import org.cishell.cibridge.core.model.AlgorithmType;
import org.cishell.cibridge.core.model.ConversionType;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataFilter;
import org.cishell.cibridge.core.model.PageInfo;
import org.cishell.cibridge.core.model.Property;
import org.cishell.cibridge.core.model.PropertyInput;
import org.cishell.cibridge.core.model.interfaces.QueryResults;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {
	private CIShellCIBridge cibridge;
	private final HashMap<String, AlgorithmInstance> algorithmInstanceCacheMap = new HashMap<>(); 
	private final HashMap<String, Integer> algorithmInstanceCount = new HashMap<>();

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@Override
	public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {

		List<AlgorithmDefinition> algorithmDefinitions = new ArrayList<>();
		BundleContext context = cibridge.getBundleContext();
		PageInfo pageInfo = new PageInfo(false,false); 
		AlgorithmDefinitionQueryResults queryResults = null;
		HashMap<String , AlgorithmDefinition> algorithmDefinitionMap = new HashMap<>();
		int limit = 0,offset = 0;
		try {
			if(filter != null) {
				System.out.println("Filter :"+filter);
				System.out.println(filter);
				if(filter.getAlgorithmDefinitionIds() != null) {
					for(String pids: filter.getAlgorithmDefinitionIds()) {
						ServiceReference[] refs = context.getServiceReferences(AlgorithmFactory.class.getName(),
								"(&(" + Constants.SERVICE_PID + "=" + pids + "))");
						if(refs != null) {
							for(ServiceReference ref : refs) {
								algorithmDefinitions.add(setAlgorithmDefinition(ref));
								//algorithmDefinitionMap.put(pids,setAlgorithmDefinition(ref));
							}

						}
					}
				}

				if(filter.getAlgorithmInstanceIds() != null) {
					for(String instanceId : filter.getAlgorithmInstanceIds()) {
						if(algorithmInstanceCacheMap.get(instanceId) != null && algorithmInstanceCacheMap.get(instanceId).getAlgorithmDefinition() != null) {
							algorithmDefinitionMap.put(algorithmInstanceCacheMap.get(instanceId).getAlgorithmDefinition().getId(),
									algorithmInstanceCacheMap.get(instanceId).getAlgorithmDefinition());
						}
					}
				}

				if(filter.getInputDataIds() != null) {
					for(String inputIds : filter.getInputDataIds()) {
						//algorithmDefinitions.add()
					}
				}

				if(filter.getInputFormats() != null) {
					for(String inpformats : filter.getInputFormats()) {
						//algorithmDefinitions.add()
					}
				}
				if(filter.getOutputFormats() != null) {
					for(String outformats : filter.getOutputFormats()) {
						//algorithmDefinitions.add()
					}
				}

				if(filter.getStates() != null) {
					for(AlgorithmState state : filter.getStates()) {
						//algorithmDefinitions.add()
					}
				}

				limit = filter.getLimit();
				offset = filter.getOffset();
			}else {
				System.out.println("Filter is empty!");
				ServiceReference[] refs = context.getServiceReferences(AlgorithmFactory.class.getName(),null);
				for(ServiceReference ref : refs) {
					algorithmDefinitions.add(setAlgorithmDefinition(ref)); 
				}
			}

			queryResults = new AlgorithmDefinitionQueryResults(algorithmDefinitions, pageInfo);
			return (AlgorithmDefinitionQueryResults) getQueryResults(queryResults,limit,offset);

		}catch(Exception e) {
			e.printStackTrace();
		}

		return queryResults;
	}

	private <T> QueryResults<T> getQueryResults(QueryResults<T> queryResults, int limit, int offset) {

		int listSize = queryResults.getResults().size();
		Boolean hasNext = false;
		Boolean hasPrevious = false;

		List<T> queryList = new ArrayList<>();
		if(offset > 0) {
			if (offset >= listSize) {
				queryList = queryResults.getResults().subList(0,0);
			}else if(limit > 0) {
				queryList = queryResults.getResults().subList(offset, Math.min(offset+limit, listSize));
				hasPrevious = true;
				if((offset+limit) < listSize) {
					hasNext = true;
				}
			}else {
				queryList = queryResults.getResults().subList(offset, listSize);
				hasPrevious = true;
			}
		}else if(limit > 0) {
			queryList = queryResults.getResults().subList(0, Math.min(limit, listSize));
			if(limit < listSize) {
				hasNext = true;
			}
		} else{
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
		
		System.out.println("AlgorithmDefinition created: "+ algorithmDefinition);
	
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

		AlgorithmDefinition algorithmDefinition = cibridge.algorithmDefinitionCacheMap.get(algorithmDefinitionId);
		List<Data> dataList = new ArrayList<>();
		List<Property> properties = new ArrayList<>();

		if(dataIds != null) {
			dataList = cibridge.cishellData.getData(new DataFilter(dataIds, null, null, null, null)) != null 
					? (cibridge.cishellData.getData(new DataFilter(dataIds, null, null, null, null)).getResults()) : null;
		}

		if(parameters != null) {
			for(PropertyInput property : parameters) {
				properties.add(new Property(property.getKey(), property.getValue()));
			}
		}

		String algorithmInstanceId = algorithmDefinitionId + (Integer.valueOf(algorithmInstanceCount.get(algorithmDefinitionId))+1);
		algorithmInstanceCount.put(algorithmDefinitionId, algorithmInstanceCount.get(algorithmDefinitionId)+1);
		AlgorithmInstance algorithmInstance = new AlgorithmInstance(algorithmInstanceId,dataList, properties, algorithmDefinition, AlgorithmState.IDLE, null, 1, null);
		algorithmInstanceCacheMap.put(algorithmInstanceId, algorithmInstance);
		System.out.println("Algorithm Instance created: "+algorithmInstance);

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

	// other implementation for DataFacade
	public AlgorithmFactory getAlgorithmFactory(String pid) {
		ServiceReference[] refs;

		return null;
	}

	@Override
	public List<AlgorithmDefinition> algorithmresults() {
		// TODO Auto-generated method stub
		return null;
	}

	public void cacheData() {
		try {
			ServiceReference[] refs = cibridge.getBundleContext().getServiceReferences(AlgorithmFactory.class.getName(),null);
			System.out.println("Caching AlgorithmDefinitions...");
			
			for(ServiceReference ref : refs) {
				if(ref.getProperty(Constants.SERVICE_PID) != null) {
					String pid = ref.getProperty(Constants.SERVICE_PID).toString();
					if(pid != null) {
						System.out.println("Caching Service pid: "+pid);
						cibridge.algorithmDefinitionCacheMap.put(pid, setAlgorithmDefinition(ref));	
						algorithmInstanceCount.put(pid, 0);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
