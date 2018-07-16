package org.cishell.cibridge.cishell.resolvers;

import java.util.List;

import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstanceQueryResults;
import org.osgi.framework.BundleContext;

public class CIShellCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {
	
	private final BundleContext context;
	
	public CIShellCIBridgeAlgorithmFacade(BundleContext context) {
		this.context=context;
	}

	@Override
	public List<AlgorithmDefinition> algorithmresults() {
		// TODO Auto-generated method stub
		return null;
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

}
