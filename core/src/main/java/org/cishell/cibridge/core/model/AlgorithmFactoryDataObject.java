package org.cishell.cibridge.core.model;

import org.cishell.framework.algorithm.AlgorithmFactory;

public class AlgorithmFactoryDataObject {
    AlgorithmDefinition algorithmDefinition;
    AlgorithmFactory algorithmFactory;

    public AlgorithmFactoryDataObject(AlgorithmDefinition algorithmDefinition, AlgorithmFactory algorithmFactory) {
        this.algorithmDefinition = algorithmDefinition;
        this.algorithmFactory = algorithmFactory;
    }

    public AlgorithmDefinition getAlgorithmDefinition() {
        return algorithmDefinition;
    }

    public AlgorithmFactory getAlgorithmFactory() {
        return algorithmFactory;
    }
}
