package org.cishell.cibridge.mock.impl;

import java.util.List;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.core.model.PropertyInput;
import org.osgi.framework.BundleContext;

public class MockCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {
	
	private final BundleContext context;
	
	public MockCIBridgeAlgorithmFacade(BundleContext context) {
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

}
