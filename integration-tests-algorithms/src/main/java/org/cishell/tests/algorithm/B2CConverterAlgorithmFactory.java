package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import java.util.Dictionary;

public class B2CConverterAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new B2CConverterAlgorithm(data, parameters, context);
    }

    private class B2CConverterAlgorithm implements Algorithm {

        private B2CConverterAlgorithm(Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
            if (data == null || data.length < 1 || data[0] == null) {
                throw new AlgorithmCreationCanceledException("Input data is null or empty");
            }

            Data datum = data[0];
            if (!datum.getFormat().equals("file:text/B")) {
                throw new AlgorithmCreationCanceledException("Unsupported file format: " + datum.getFormat());
            }
        }

        public Data[] execute() {
            Data[] data = new Data[1];
            data[0] = new BasicData(null, "some data in format C", "file:text/C");
            return data;
        }
    }
}