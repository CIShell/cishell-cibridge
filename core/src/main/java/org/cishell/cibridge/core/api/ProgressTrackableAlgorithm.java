package org.cishell.cibridge.core.api;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;

public class ProgressTrackableAlgorithm implements Algorithm, ProgressTrackable {
    private Algorithm algorithm;
    private String AlgorithmInstanceId;
    private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

    public ProgressTrackableAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithmInstanceId() {
        return AlgorithmInstanceId;
    }

    public void setAlgorithmInstanceId(String algorithmInstanceId) {
        AlgorithmInstanceId = algorithmInstanceId;
    }

    @Override
    public Data[] execute() throws AlgorithmExecutionException {
        return algorithm.execute();
    }

    @Override
    public void setProgressMonitor(ProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    @Override
    public ProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }
}