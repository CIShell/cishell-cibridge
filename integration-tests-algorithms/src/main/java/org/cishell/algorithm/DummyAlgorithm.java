package org.cishell.algorithm;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import java.util.Dictionary;


public class DummyAlgorithm implements Algorithm {

    private LogService logger;

    public DummyAlgorithm(Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() {
        return new BasicData[0];
    }
}
