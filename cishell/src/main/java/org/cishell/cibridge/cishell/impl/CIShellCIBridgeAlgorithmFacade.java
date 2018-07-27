package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.core.model.AlgorithmType;
import org.cishell.cibridge.core.model.ConversionType;
import org.cishell.cibridge.core.model.PageInfo;
import org.cishell.cibridge.core.model.PropertyInput;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {
	private CIShellCIBridge cibridge;

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@Override
	public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {
	
		List<AlgorithmDefinition> algorithmDefinitions = new ArrayList<>();
		BundleContext context = cibridge.getBundleContext();
		PageInfo pageInfo = new PageInfo(false,false); 
		AlgorithmDefinitionQueryResults queryResults = null;
		try {
			if(filter != null) {
				System.out.println("Filter :"+filter);
				System.out.println(filter);
				/*if(filter.getAlgorithmDefinitionIds() != null) {
					for(String pids: filter.getAlgorithmDefinitionIds()) {
						ServiceReference[] refs = context.getServiceReferences(AlgorithmFactory.class.getName(),
								"(&(" + Constants.SERVICE_PID + "=" + pids + "))");
						for(ServiceReference ref : refs) {
							algorithmDefinitions.add(setAlgorithmDefinition(ref)); 
						}
					}
				}*/
			}else {
				System.out.println("Filter is empty!");
				ServiceReference[] refs = context.getServiceReferences(AlgorithmFactory.class.getName(),null);
				for(ServiceReference ref : refs) {
					algorithmDefinitions.add(setAlgorithmDefinition(ref)); 
				}
			}
			queryResults = new AlgorithmDefinitionQueryResults(algorithmDefinitions, pageInfo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return queryResults;
	}

	private AlgorithmDefinition setAlgorithmDefinition(ServiceReference ref) {
		
		AlgorithmDefinition algorithmDefinition  = null;
		algorithmDefinition = 
				new AlgorithmDefinition("1",
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
		// TODO Auto-generated method stub
		return null;
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

}
