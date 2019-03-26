package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import java.util.Dictionary;

public class StandardAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new StandardAlgorithm(data, parameters, context);
    }

    private class StandardAlgorithm implements Algorithm, ProgressTrackable {

        private ProgressMonitor progressMonitor;

        private StandardAlgorithm(Data[] data, Dictionary parameters, CIShellContext cishellContext) {
        }

        @Override
        public Data[] execute() {

            getProgressMonitor().start(ProgressMonitor.CANCELLABLE | ProgressMonitor.PAUSEABLE | ProgressMonitor.WORK_TRACKABLE, 100);
            for(int i = 10; i <= 100; i += 10){
                getProgressMonitor().worked(i);
            }
            getProgressMonitor().done();

            return new BasicData[0];
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
}