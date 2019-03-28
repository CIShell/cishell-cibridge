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

            int quantum = 500;
            int work = 10;

            System.out.println("Starting algorithm");
            progressMonitor.start(CANCELLABLE | PAUSEABLE | WORK_TRACKABLE, work);

            for (int i = 1; i <= work; i += 1) {
                if (progressMonitor.isCanceled()) {
                    System.out.println("Algorithm is canceled");
                    return new BasicData[0];
                }

                while (progressMonitor.isPaused()) {
                    if (progressMonitor.isCanceled()) {
                        System.out.println("Algorithm is canceled");
                        return new BasicData[0];
                    }

                    System.out.println("Algorithm is paused before completing milestone: " + i);
                    sleep(quantum);
                }

                sleep(quantum);
                System.out.println("completed milestone: " + i);
                progressMonitor.worked(i);
            }

            System.out.println("algorithm finished");
            progressMonitor.done();

            return new BasicData[0];
        }

        private void sleep(int period) {
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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