package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import java.util.Dictionary;

public class ErringAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ErringAlgorithm(data, parameters, context);
    }

    private class ErringAlgorithm implements Algorithm {

        private ErringAlgorithm(Data[] data, Dictionary parameters, CIShellContext cishellContext) {
        }

        @Override
        public Data[] execute() throws AlgorithmExecutionException {
            int quantum = 200;
            try {
                Thread.sleep(quantum);
            } catch (Exception e) {
                throw new AlgorithmExecutionException(e);
            }
            throw new AlgorithmExecutionException("The algorithm intentionally threw an error. Please ignore.");
        }
    }
}