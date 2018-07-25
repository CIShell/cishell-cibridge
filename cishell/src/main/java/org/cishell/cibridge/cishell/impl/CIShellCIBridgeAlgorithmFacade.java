package org.cishell.cibridge.cishell.impl;

import java.util.List;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.cishell.data.AlgorithmDefinitionCIShell;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.core.model.PropertyInput;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.apache.felix.framework.Felix;

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {
	private final BundleContext context;
	private CIShellCIBridge cibridge;
	AlgorithmDefinitionCIShell algorithmDefinitionCIShell = new AlgorithmDefinitionCIShell();

	public CIShellCIBridgeAlgorithmFacade(BundleContext context) {
		this.context = context;
	}

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@Override
	public List<AlgorithmDefinition> algorithmresults() {
		// TODO Auto-generated method stub
		System.out.println("hitting here11");
		return algorithmDefinitionCIShell.getAlgorithmresults();
	}

	@Override
	public AlgorithmDefinitionQueryResults getAlgorithmDefinitions(AlgorithmFilter filter) {
		// TODO Auto-generated method stub
		return null;
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

		try {
			refs = context.getServiceReferences(AlgorithmFactory.class.getName(),
					"(&(" + Constants.SERVICE_PID + "=" + pid + "))");
			if (refs != null && refs.length > 0) {
				return (AlgorithmFactory) context.getService(refs[0]);
			} else {
				return null;
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
