package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.junit.Test;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CIShellCIBridgeDataConversionIT extends IntegrationTestCase {

    private CIShellCIBridgeDataFacade dataFacade = getCIShellCIBridge().cishellData;
    private CIShellCIBridgeAlgorithmFacade algorithmFacade = getCIShellCIBridge().cishellAlgorithm;
    private CIShellCIBridgeSchedulerFacade schedulerFacade = getCIShellCIBridge().cishellScheduler;

    @Test
    public void findConvertersByData() {

    }

    @Test
    public void findConvertersByFormat() {
        String inputDataFormat = "file:text/A";
        String outputDataFormat = "file:text/C";

        List<AlgorithmDefinition> listOfConverterAlgorithms = dataFacade.findConvertersByFormat(inputDataFormat, outputDataFormat);
        assertTrue(listOfConverterAlgorithms.size() > 0);

        AlgorithmDefinition algorithmDefinition = listOfConverterAlgorithms.get(0);

        DataProperties properties = new DataProperties();
        properties.setFormat("file:text/A");

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = dataFacade.uploadData(dataFileUrl.getFile(), properties);

        AlgorithmInstance algorithmInstance = algorithmFacade.createAlgorithm(algorithmDefinition.getId(), Collections.singletonList(data.getId()), null);

        schedulerFacade.runAlgorithmNow(algorithmInstance.getId());
        System.out.println(algorithmInstance);

        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == AlgorithmState.FINISHED));
    }
}
