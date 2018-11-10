package org.cishell.cibridge.core.model;

import org.cishell.framework.algorithm.Algorithm;

public class AlgorithmDataObject {

    private Algorithm algorithm;
    private AlgorithmInstance algorithmInstance;

    public AlgorithmDataObject(Algorithm algorithm, AlgorithmInstance algorithmInstance) {
        this.algorithm = algorithm;
        this.algorithmInstance = algorithmInstance;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public AlgorithmInstance getAlgorithmInstance() {
        return algorithmInstance;
    }
}
