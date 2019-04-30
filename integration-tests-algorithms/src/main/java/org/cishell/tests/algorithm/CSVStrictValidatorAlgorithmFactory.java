package org.cishell.tests.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import java.io.File;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class CSVStrictValidatorAlgorithmFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new CSVStrictValidatorAlgorithm(data, parameters, context);
    }

    private class CSVStrictValidatorAlgorithm implements Algorithm {

        private String filepath = null;
        private Dictionary<String, Object> metadata = null;

        private CSVStrictValidatorAlgorithm(Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
            assert data != null && data.length > 0;
            this.filepath = data[0].getData().toString();
            this.metadata = new Hashtable<>();

            Enumeration<String> iterator = data[0].getMetadata().keys();
            while (iterator.hasMoreElements()) {
                String key = iterator.nextElement();
                metadata.put(key, data[0].getMetadata().get(key));
            }
        }

        public Data[] execute() {
            return new Data[]{new BasicData(metadata, new File(filepath), "file:text/csv")};
        }
    }

}