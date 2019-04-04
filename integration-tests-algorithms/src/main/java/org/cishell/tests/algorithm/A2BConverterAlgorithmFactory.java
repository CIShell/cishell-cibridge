package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import java.util.Dictionary;

public class A2BConverterAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new A2BConverterAlgorithm(data, parameters, context);
    }

    private class A2BConverterAlgorithm implements Algorithm {

        private A2BConverterAlgorithm(Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
            if (data == null || data.length < 1 || data[0] == null) {
                throw new AlgorithmCreationCanceledException("Input data is null or empty");
            }

            Data datum = data[0];
            if (datum.getFormat() == null || !datum.getFormat().equals("file:text/A")) {
                throw new AlgorithmCreationCanceledException("Unsupported file format: " + datum.getFormat());
            }
        }

        public Data[] execute() throws AlgorithmExecutionException {
            Data[] data = new Data[1];
            data[0] = new BasicData(null, "some data in format B", "file:text/B");
            return data;
        }
    }
}