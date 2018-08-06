package org.cishell.cibridge.core.model;

import org.cishell.cibridge.core.wrapper.ProgressTrackableAlgorithm;

public class AlgorithmDataObject {

    private ProgressTrackableAlgorithm algorithm;
    private AlgorithmInstance algorithmInstance;

    public AlgorithmDataObject(ProgressTrackableAlgorithm algorithm, AlgorithmInstance algorithmInstance){
        this.algorithm = algorithm;
        this.algorithmInstance = algorithmInstance;
    }

    public ProgressTrackableAlgorithm getAlgorithm() {
        return algorithm;
    }

    public AlgorithmInstance getAlgorithmInstance(){
        return algorithmInstance;
    }
}
