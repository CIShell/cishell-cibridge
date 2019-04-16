package org.cishell.cibridge.cishell.impl;

import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.model.CIShellCIBridgeAlgorithmInstance;
import org.cishell.cibridge.core.model.*;
import org.cishell.framework.algorithm.Algorithm;
import org.junit.After;
import org.junit.Test;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CIShellCIBridgeAlgorithmFacadeIT extends CIShellCIBridgeBaseIT {

    private CIShellCIBridgeAlgorithmFacade algorithmFacade = getCIShellCIBridge().cishellAlgorithm;

    private AlgorithmDefinition getAlgorithmDefinition(String pid) {
        return algorithmFacade.getAlgorithmDefinitionMap().get(pid);
    }

    @Test
    public void validateStringValuePropertiesOfAlgorithmDefinitions() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        AlgorithmDefinition algorithmDefinition = getAlgorithmDefinition(pid);
        assertNotNull(algorithmDefinition);
        assertEquals(pid, algorithmDefinition.getId());
        assertEquals("Run/Run...", algorithmDefinition.getMenuPath());
        assertEquals("Standard Algorithm", algorithmDefinition.getLabel());
        assertEquals("Standard type algorithm which doesn't do anything", algorithmDefinition.getDescription());
        assertEquals("Java", algorithmDefinition.getWrittenIn());
        assertEquals("Hardik Rakholiya", algorithmDefinition.getAuthors());
        assertEquals("Hardik Rakholiya", algorithmDefinition.getImplementers());
        assertEquals("Hardik Rakholiya", algorithmDefinition.getIntegrators());
        assertEquals("https://www.standard-algorithm.tests.cishell.github.io", algorithmDefinition.getDocumentationUrl());
        assertEquals("Cyberinfrastructure Shell", algorithmDefinition.getReference());
        assertEquals("https://cishell.github.io", algorithmDefinition.getReferenceUrl());
    }

    @Test
    public void validateTypesOfAlgorithmDefinitions() {
        String standard = "org.cishell.tests.algorithm.StandardAlgorithm";
        String converter = "org.cishell.tests.algorithm.LossyConverterAlgorithm";
        String validator = "org.cishell.tests.algorithm.ValidatorAlgorithm";

        AlgorithmDefinition standardAlgorithmDefinition = getAlgorithmDefinition(standard);
        assertNotNull(standardAlgorithmDefinition);
        assertEquals(standard, standardAlgorithmDefinition.getId());
        assertEquals(AlgorithmType.STANDARD, standardAlgorithmDefinition.getType());

        AlgorithmDefinition converterAlgorithmDefinition = getAlgorithmDefinition(converter);
        assertNotNull(converterAlgorithmDefinition);
        assertEquals(converter, converterAlgorithmDefinition.getId());
        assertEquals(AlgorithmType.CONVERTER, converterAlgorithmDefinition.getType());

        AlgorithmDefinition validatorAlgorithmDefinition = getAlgorithmDefinition(validator);
        assertNotNull(validatorAlgorithmDefinition);
        assertEquals(validator, validatorAlgorithmDefinition.getId());
        assertEquals(AlgorithmType.VALIDATOR, validatorAlgorithmDefinition.getType());
    }

    @Test
    public void validateInputAndOutputFormatsOfAlgorithmDefinitions() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";

        AlgorithmDefinition algorithmDefinition = getAlgorithmDefinition(pid);

        assertNotNull(algorithmDefinition);
        assertEquals(2, algorithmDefinition.getInData().size());
        assertEquals("text/A", algorithmDefinition.getInData().get(0));
        assertEquals("text/B", algorithmDefinition.getInData().get(1));
        assertEquals(2, algorithmDefinition.getOutData().size());
        assertEquals("text/C", algorithmDefinition.getOutData().get(0));
        assertEquals("text/D", algorithmDefinition.getOutData().get(1));
    }

    @Test
    public void validateConversionTypesOfAlgorithmDefinitions() {
        String lossy = "org.cishell.tests.algorithm.LossyConverterAlgorithm";
        String lossless = "org.cishell.tests.algorithm.LosslessConverterAlgorithm";

        AlgorithmDefinition lossyAlgorithmDefinition = getAlgorithmDefinition(lossy);
        assertNotNull(lossyAlgorithmDefinition);
        assertEquals(lossy, lossyAlgorithmDefinition.getId());
        assertEquals(ConversionType.LOSSY, lossyAlgorithmDefinition.getConversion());

        AlgorithmDefinition losslessAlgorithmDefinition = getAlgorithmDefinition(lossless);
        assertNotNull(losslessAlgorithmDefinition);
        assertEquals(lossless, losslessAlgorithmDefinition.getId());
        assertEquals(ConversionType.LOSSLESS, losslessAlgorithmDefinition.getConversion());
    }

    @Test
    public void validateRemoteableValuesOfAlgorithmDefinitions() {
        String remoteable = "org.cishell.tests.algorithm.ValidatorAlgorithm";
        String nonRemoteable = "org.cishell.tests.algorithm.LosslessConverterAlgorithm";

        AlgorithmDefinition remoteableAlgorithmDefinition = getAlgorithmDefinition(remoteable);
        assertNotNull(remoteableAlgorithmDefinition);
        assertEquals(remoteable, remoteableAlgorithmDefinition.getId());
        assertTrue(remoteableAlgorithmDefinition.getRemoteable());

        AlgorithmDefinition nonRemoteableAlgorithmDefinition = getAlgorithmDefinition(nonRemoteable);
        assertNotNull(nonRemoteableAlgorithmDefinition);
        assertEquals(nonRemoteable, nonRemoteableAlgorithmDefinition.getId());
        assertFalse(nonRemoteableAlgorithmDefinition.getRemoteable());
    }

    @Test
    public void validateOtherPropertiesOfAlgorithmDefinition() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        AlgorithmDefinition algorithmDefinition = getAlgorithmDefinition(pid);
        assertNotNull(algorithmDefinition);
        assertEquals(pid, algorithmDefinition.getId());
        assertEquals("its_value", algorithmDefinition.getOtherProperties().stream().collect(Collectors.toMap(Property::getKey, Property::getValue)).get("other_property"));
    }

    @Test
    public void getAlgorithmDefinitionsWithEmptyFilter() {
        //querying with empty filter should return everything including 4 dummy algorithms
        QueryResults<AlgorithmDefinition> queryResults = algorithmFacade.getAlgorithmDefinitions(new AlgorithmFilter());
        assertNotNull(queryResults);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertTrue(LIST_OF_INTEGRATION_TESTS_ALGORITHMS_PIDS.size() <= queryResults.getResults().size());
    }

    @Test
    public void getAlgorithmDefinitionsWithSpecifiedAlgorithmDefinitionIDs() {

        AlgorithmFilter filter = new AlgorithmFilter();
        QueryResults<AlgorithmDefinition> queryResults;
        String pid1 = "org.cishell.tests.algorithm.StandardAlgorithm";
        String pid2 = "org.cishell.tests.algorithm.ValidatorAlgorithm";

        //query for either of the two above pids should return 2 results
        filter.setAlgorithmDefinitionIds(Arrays.asList(pid1, pid2));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(2, queryResults.getResults().size());
        assertTrue(queryResults.getResults().stream().map(AlgorithmDefinition::getId).collect(Collectors.toSet()).contains(pid1));
        assertTrue(queryResults.getResults().stream().map(AlgorithmDefinition::getId).collect(Collectors.toSet()).contains(pid2));

        //query for just one should return 1 result
        filter.setAlgorithmDefinitionIds(Collections.singletonList(pid1));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(1, queryResults.getResults().size());
        assertTrue(queryResults.getResults().stream().map(AlgorithmDefinition::getId).collect(Collectors.toSet()).contains(pid1));

        //query for non-existent pid should return nothing
        filter.setAlgorithmDefinitionIds(Collections.singletonList("someNonExistentID"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(0, queryResults.getResults().size());

    }

    @Test
    public void getAlgorithmDefinitionsWithSpecifiedAlgorithmInstanceIDs() {
        AlgorithmFilter filter = new AlgorithmFilter();
        QueryResults<AlgorithmDefinition> queryResults;
        String pid1 = "org.cishell.tests.algorithm.StandardAlgorithm";
        String pid2 = "org.cishell.tests.algorithm.ValidatorAlgorithm";

        AlgorithmInstance algorithmInstance1 = algorithmFacade.createAlgorithm(pid1, null, null);
        filter.setAlgorithmInstanceIds(Collections.singletonList(algorithmInstance1.getId()));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(1, queryResults.getResults().size());
        assertTrue(queryResults.getResults().stream().map(AlgorithmDefinition::getId).collect(Collectors.toSet()).contains(pid1));

        AlgorithmInstance algorithmInstance2 = algorithmFacade.createAlgorithm(pid2, null, null);
        filter.setAlgorithmInstanceIds(Arrays.asList(algorithmInstance1.getId(), algorithmInstance2.getId()));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(2, queryResults.getResults().size());
        assertTrue(queryResults.getResults().stream().map(AlgorithmDefinition::getId).collect(Collectors.toSet()).contains(pid1));
        assertTrue(queryResults.getResults().stream().map(AlgorithmDefinition::getId).collect(Collectors.toSet()).contains(pid2));

        filter.setAlgorithmInstanceIds(Collections.singletonList("someNonExistentID"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getAlgorithmDefinitionsForSpecifiedInputFormats() {
        AlgorithmFilter filter = new AlgorithmFilter();
        QueryResults<AlgorithmDefinition> queryResults;

        //querying with empty list should not return anything
        filter.setInputFormats(new ArrayList<>());
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(0, queryResults.getResults().size());

        //querying with text csv format
        filter.setInputFormats(Collections.singletonList("file:text/csv"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(2, queryResults.getResults().size());

        //querying with either text/xml or text/csv format should return 3 results
        filter.setInputFormats(Arrays.asList("file:text/csv", "file:text/xml"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(3, queryResults.getResults().size());

        //querying with non-existent format should not return anything
        filter.setInputFormats(Collections.singletonList("someNonExistentDataFormat"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getAlgorithmDefinitionsForSpecifiedOutputFormats() {
        AlgorithmFilter filter = new AlgorithmFilter();
        QueryResults<AlgorithmDefinition> queryResults;

        //querying with empty list should not return anything
        filter.setOutputFormats(new ArrayList<>());
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(0, queryResults.getResults().size());

        //querying with file:bin/graph format
        filter.setOutputFormats(Collections.singletonList("file:bin/graph"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(2, queryResults.getResults().size());

        //querying with either text/xml or file:bin/graph should return 3 results
        filter.setOutputFormats(Arrays.asList("file:bin/graph", "file:text/xml"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(3, queryResults.getResults().size());

        //querying with non-existent format should not return anything
        filter.setOutputFormats(Collections.singletonList("someNonExistentDataFormat"));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getAlgorithmDefinitionsForInputDataIDs() {
        DataProperties dataProperties = new DataProperties();
        dataProperties.setFormat("file:text/xml");

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties);

        AlgorithmFilter filter = new AlgorithmFilter();
        QueryResults<AlgorithmDefinition> queryResults;

        filter.setInputDataIds(Collections.singletonList(data.getId()));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals("org.cishell.tests.algorithm.ValidatorAlgorithm", queryResults.getResults().get(0).getId());
    }

    @Test
    public void getAlgorithmDefinitionsOnPropertyValues() {
        QueryResults<AlgorithmDefinition> queryResults;
        AlgorithmFilter filter = new AlgorithmFilter();

        filter.setProperties(Collections.singletonList(new PropertyInput("some_property", "some_value")));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(2, queryResults.getResults().size());

        filter.setProperties(Arrays.asList(new PropertyInput("other_property", "its_value"), new PropertyInput("some_property", "some_value")));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(1, queryResults.getResults().size());

        filter.setProperties(Arrays.asList(new PropertyInput("other_property", "its_value"), new PropertyInput("other_property", "some_value")));
        queryResults = algorithmFacade.getAlgorithmDefinitions(filter);
        assertEquals(2, queryResults.getResults().size());
    }

    @Test
    public void validateAlgorithmDefinitionAddedSubscription() {
        TestSubscriber<AlgorithmDefinition> testSubscriber = new TestSubscriber<>();
        algorithmFacade.algorithmDefinitionAdded().subscribe(testSubscriber);

        String inputDataFormat = "file:text/A";
        String outputDataFormat = "file:text/C";

        List<AlgorithmDefinition> listOfConverterAlgorithms = getCIShellCIBridge().cishellData.findConvertersByFormat(inputDataFormat, outputDataFormat);
        assertTrue(listOfConverterAlgorithms.size() > 0);
        AlgorithmDefinition algorithmDefinition = listOfConverterAlgorithms.get(0);

        testSubscriber.awaitCount(1);
        AlgorithmDefinition cachedAlgorithmDefintion = testSubscriber.values().get(0);
        assertEquals(algorithmDefinition, cachedAlgorithmDefintion);
    }

    //todo write tests for filtering algorithm instances
    @Test
    public void getAlgorithmInstances() {

    }

    @Test
    public void createAlgorithm() {
        String pid = "org.cishell.tests.algorithm.StandardAlgorithm";
        assertNotNull(getAlgorithmDefinition(pid));
        AlgorithmInstance algorithmInstance = algorithmFacade.createAlgorithm(pid, null, null);
        assertNotNull(algorithmInstance);
        assertTrue(algorithmFacade.getAlgorithmInstanceMap().containsKey(algorithmInstance.getId()));

        if (algorithmInstance instanceof CIShellCIBridgeAlgorithmInstance) {
            Algorithm algorithm = ((CIShellCIBridgeAlgorithmInstance) algorithmInstance).getAlgorithm();
            assertTrue(algorithmFacade.getCIShellAlgorithmCIBridgeAlgorithmMap().containsKey(algorithm));
            assertEquals(algorithmInstance, algorithmFacade.getCIShellAlgorithmCIBridgeAlgorithmMap().get(algorithm));
        }
    }

    @Test
    public void uncacheAlgorithmDefinition() {
        //todo test uncaching of the algorithms
    }

    @After
    public void tearDown() {
        List<String> dataIdList = new LinkedList<>(getCIShellCIBridge().cishellData.getCIBridgeDataMap().keySet());
        for (String dataId : dataIdList) {
            getCIShellCIBridge().cishellData.removeData(dataId);
        }

        assertEquals(0, getDataManagerService().getAllData().length);
        assertEquals(0, getCIShellCIBridge().cishellData.getCIBridgeDataMap().size());
        assertEquals(0, getCIShellCIBridge().cishellData.getCIShellDataCIBridgeDataMap().size());

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
