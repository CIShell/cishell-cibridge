package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import java.util.Dictionary;

public class ValidatorAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ValidatorAlgorithm(data, parameters, context);
    }

    private class ValidatorAlgorithm implements Algorithm {

        private ValidatorAlgorithm(Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        }

        public Data[] execute() {
            return new BasicData[0];
        }
    }

}