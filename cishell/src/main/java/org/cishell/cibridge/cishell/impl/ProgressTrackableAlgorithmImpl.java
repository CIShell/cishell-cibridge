package org.cishell.cibridge.cishell.impl;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;

public class ProgressTrackableAlgorithmImpl implements Algorithm, ProgressTrackable {

    private final Algorithm algorithm;
    private ProgressMonitor progressMonitor;

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public ProgressTrackableAlgorithmImpl(Algorithm algorithm) {
        this.algorithm = algorithm;
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
