package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
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
        public Data[] execute() {
            int quantum = 200;
            int work = 10;
            Data[] data;

            try {
                progressMonitor.start(CANCELLABLE | PAUSEABLE | WORK_TRACKABLE, work);

                for (int i = 1; i <= work; i += 1) {

                    if (progressMonitor.isCanceled()) {
                        return new BasicData[0];
                    }

                    if (progressMonitor.isPaused()) {
                        synchronized (progressMonitor) {
                            progressMonitor.wait();
                        }
                    }

                    Thread.sleep(quantum);
                    progressMonitor.worked(i);
                }

                progressMonitor.done();
                data = new BasicData[0];

            } catch (Exception e) {
                throw new RuntimeException(e);
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