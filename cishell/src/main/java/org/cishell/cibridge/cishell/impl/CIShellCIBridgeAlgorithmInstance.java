package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.framework.algorithm.Algorithm;

import java.util.HashMap;

public class CIShellCIBridgeAlgorithmInstance extends AlgorithmInstance {
    private Algorithm algorithm;
    private static final HashMap<String, Long> algorithmInstanceCount = new HashMap<>();

    public CIShellCIBridgeAlgorithmInstance(AlgorithmDefinition algorithmDefinition, Algorithm algorithm) {
        super(generateId(algorithmDefinition), algorithmDefinition);
        this.algorithm = algorithm;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    private static String generateId(AlgorithmDefinition algorithmDefinition) {
        String algorithmDefinitionId = algorithmDefinition.getId();
        long newCount;

        if (algorithmInstanceCount.get(algorithmDefinitionId) != null) {
            newCount = algorithmInstanceCount.get(algorithmDefinitionId) + 1;
        } else {
            newCount = 1L;
        }

        algorithmInstanceCount.put(algorithmDefinitionId, newCount);
        return algorithmDefinitionId + "_" + newCount;
    }

    @Override
    public String toString() {
        return "CIShellCIBridgeAlgorithmInstance{" +
                "algorithm=" + algorithm +
                "} " + super.toString();
    }
}
