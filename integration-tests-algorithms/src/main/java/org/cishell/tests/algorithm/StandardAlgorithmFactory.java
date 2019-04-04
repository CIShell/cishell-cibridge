package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.*;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import java.util.Dictionary;

import static org.cishell.framework.algorithm.ProgressMonitor.*;

public class StandardAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new StandardAlgorithm(data, parameters, context);
    }

    private class StandardAlgorithm implements Algorithm, ProgressTrackable {

        private ProgressMonitor progressMonitor;

        private StandardAlgorithm(Data[] data, Dictionary parameters, CIShellContext cishellContext) {
        }

        @Override
        public Data[] execute() throws AlgorithmExecutionException {
            int quantum = 200;
            int work = 100;
            Data[] data;

            try {
                progressMonitor.start(CANCELLABLE | PAUSEABLE | WORK_TRACKABLE, work);

                for (int i = 1; i <= work; i += 1) {
                    //before pausing check if the algorithm is cancelled
                    if (progressMonitor.isCanceled()) {
                        return new BasicData[0];
                    }

                    //pause if required
                    if (progressMonitor.isPaused()) {
                        synchronized (progressMonitor) {
                            progressMonitor.wait();
                        }
                    }

                    //after pausing check if the algorithm is cancelled
                    if (progressMonitor.isCanceled()) {
                        return new BasicData[0];
                    }

                    //sleep is like work here
                    Thread.sleep(quantum);

                    //update the progress
                    progressMonitor.worked(i);
                }

                //mark finished and set result
                progressMonitor.done();
                data = new BasicData[0];

            } catch (Exception e) {
                throw new AlgorithmExecutionException(e);
            }

            return data;
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