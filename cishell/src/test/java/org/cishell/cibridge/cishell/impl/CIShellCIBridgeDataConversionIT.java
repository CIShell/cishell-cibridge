package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.core.model.*;
import org.cishell.framework.algorithm.Algorithm;
import org.junit.After;
import org.junit.Test;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CIShellCIBridgeDataConversionIT extends CIShellCIBridgeBaseIT {

    @Test
    public void findConvertersByData() {
        DataProperties properties = new DataProperties();
        properties.setFormat("file:text/A");

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = dataFacade.uploadData(dataFileUrl.getFile(), properties);

        String outputDataFormat = "file:text/C";

        List<AlgorithmDefinition> listOfConverterAlgorithms = dataFacade.findConverters(data.getId(), outputDataFormat);
        assertTrue(listOfConverterAlgorithms.size() > 0);

        AlgorithmDefinition algorithmDefinition = listOfConverterAlgorithms.get(0);

        AlgorithmInstance algorithmInstance = algorithmFacade.createAlgorithm(algorithmDefinition.getId(), Collections.singletonList(data.getId()), null);

        schedulerFacade.runAlgorithmNow(algorithmInstance.getId());

        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == AlgorithmState.FINISHED));

        DataFilter dataFilter = new DataFilter();
        dataFilter.setFormats(Collections.singletonList("file:text/C"));

        assertEquals(1, algorithmInstance.getInData().size());
        assertEquals(data, algorithmInstance.getInData().get(0));
        assertEquals(1, algorithmInstance.getOutData().size());
        assertEquals("file:text/C", algorithmInstance.getOutData().get(0).getFormat());
        assertEquals(data.getId(), algorithmInstance.getOutData().get(0).getParentDataId());
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

        assertTrue(waitTillSatisfied(algorithmInstance, ai -> ai.getState() == AlgorithmState.FINISHED));

        assertEquals(1, algorithmInstance.getInData().size());
        assertEquals(data, algorithmInstance.getInData().get(0));
        assertEquals(1, algorithmInstance.getOutData().size());
        assertEquals("file:text/C", algorithmInstance.getOutData().get(0).getFormat());
        assertEquals(data.getId(), algorithmInstance.getOutData().get(0).getParentDataId());
    }

    @After
    public void tearDown() {

        List<String> dataIdList = new LinkedList<>(getCIShellCIBridge().cishellData.getCIBridgeDataMap().keySet());
        for (String dataId : dataIdList) {
            getCIShellCIBridge().cishellData.removeData(dataId);
        }

        assertEquals(0, getDataManagerService().getAllData().length);
        assertEquals(0, dataFacade.getCIBridgeDataMap().size());
        assertEquals(0, dataFacade.getCIShellDataCIBridgeDataMap().size());

        List<String> algorithmInstanceIdList = new LinkedList<>(algorithmFacade.getAlgorithmInstanceMap().keySet());
        for (String algorithmInstanceId : algorithmInstanceIdList) {
            algorithmFacade.getAlgorithmDefinitionMap().remove(algorithmInstanceId);
        }

        List<Algorithm> algorithmList = new LinkedList<>(algorithmFacade.getCIShellAlgorithmCIBridgeAlgorithmMap().keySet());
        for (Algorithm algorithm : algorithmList) {
            algorithmFacade.getCIShellAlgorithmCIBridgeAlgorithmMap().remove(algorithm);
        }

    }
}
