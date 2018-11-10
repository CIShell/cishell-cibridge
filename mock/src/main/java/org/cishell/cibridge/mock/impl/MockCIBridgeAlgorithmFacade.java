package org.cishell.cibridge.mock.impl;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.util.List;

public class MockCIBridgeAlgorithmFacade implements CIBridge.AlgorithmFacade {

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
