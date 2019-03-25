package org.cishell.cibridge.cishell.impl;

import io.reactivex.subscribers.TestSubscriber;
import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.cishell.framework.data.DataProperty.*;
import static org.junit.Assert.*;

public class CIShellCIBridgeDataFacadeIT extends IntegrationTestCase {

    private CIShellCIBridgeDataFacade cishellCIBridgeDataFacade = getCIShellCIBridge().cishellData;

    @Test(expected = IllegalArgumentException.class)
    public void uploadNonExistentFile() {
        cishellCIBridgeDataFacade.uploadData("SomeNonExistentFile.txt", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void uploadDirectory() {
        cishellCIBridgeDataFacade.uploadData(System.getProperty("user.dir"), null);
    }

    @Test
    public void uploadFileWithoutProperties() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        assertNotNull(data);
        assertNotNull(data.getId());
        assertNotNull(data.getFormat());
        assertEquals("file-ext:txt", data.getFormat());

        if (data instanceof CIShellCIBridgeData) {
            CIShellCIBridgeData ciShellCIBridgeData = (CIShellCIBridgeData) data;
            assertNotNull(ciShellCIBridgeData.getCIShellData());
            assertEquals("file-ext:txt", ciShellCIBridgeData.getCIShellData().getFormat());
            assertEquals(ciShellCIBridgeData.getCIShellData(), getDataManagerService().getAllData()[0]);
        }

    }

    @Test
    public void uploadFileWithProperties() {
        URL parentDataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(parentDataFileUrl);
        Data parentData = cishellCIBridgeDataFacade.uploadData(parentDataFileUrl.getFile(), null);

        String format = "file:text/csv";
        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
        DataType dataType = DataType.DATABASE;
        PropertyInput customProperty = new PropertyInput("CustomProperty", "SomeValue");

        URL dataFileUrl = getClass().getClassLoader().getResource("LaszloBarabasi.csv");
        assertNotNull(dataFileUrl);
        DataProperties dataProperties = new DataProperties();
        dataProperties.setFormat(format);
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setParent(parentData.getId());
        dataProperties.setOtherProperties(Collections.singletonList(customProperty));
        Data data = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties);

        assertNotNull(data);
        assertNotNull(data.getId());
        assertNotNull(data.getFormat());

        assertEquals(format, data.getFormat());
        assertEquals(label, data.getLabel());
        assertEquals(name, data.getName());
        assertEquals(dataType, data.getType());
        assertEquals(parentData.getId(), data.getParentDataId());
        assertEquals(customProperty.getKey(), data.getOtherProperties().get(0).getKey());
        assertEquals(customProperty.getValue(), data.getOtherProperties().get(0).getValue());

        if (data instanceof CIShellCIBridgeData) {
            CIShellCIBridgeData ciShellCIBridgeData = (CIShellCIBridgeData) data;
            assertNotNull(ciShellCIBridgeData.getCIShellData());
            org.cishell.framework.data.Data ciShellData = ciShellCIBridgeData.getCIShellData();
            assertEquals(format, ciShellData.getFormat());
            assertEquals(label, ciShellData.getMetadata().get(LABEL));
            assertEquals(name, ciShellData.getMetadata().get("Name"));
            assertEquals(dataType.name(), ciShellData.getMetadata().get(TYPE));
            assertEquals(((CIShellCIBridgeData) parentData).getCIShellData(), ciShellData.getMetadata().get(PARENT));
            assertEquals("SomeValue", ciShellData.getMetadata().get("CustomProperty"));
        }
    }

    @Test
    public void removeData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        assertTrue(cishellCIBridgeDataFacade.removeData(data.getId()));
        assertEquals(0, getDataManagerService().getAllData().length);

        assertFalse(cishellCIBridgeDataFacade.removeData("SomeNonExistentId"));
    }

    @Test
    public void updateData() {

        URL parentDataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(parentDataFileUrl);
        Data parentData = cishellCIBridgeDataFacade.uploadData(parentDataFileUrl.getFile(), null);

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
        DataType dataType = DataType.DATABASE;
        PropertyInput customProperty = new PropertyInput("CustomProperty", "SomeValue");

        DataProperties dataProperties = new DataProperties();
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setParent(parentData.getId());
        dataProperties.setOtherProperties(Collections.singletonList(customProperty));
        assertTrue(cishellCIBridgeDataFacade.updateData(data.getId(), dataProperties));

        assertEquals(label, data.getLabel());
        assertEquals(name, data.getName());
        assertEquals(dataType, data.getType());
        assertEquals(parentData.getId(), data.getParentDataId());
        assertEquals(1, data.getOtherProperties().size());
        assertEquals(customProperty.getKey(), data.getOtherProperties().get(0).getKey());
        assertEquals(customProperty.getValue(), data.getOtherProperties().get(0).getValue());

        if (data instanceof CIShellCIBridgeData) {
            CIShellCIBridgeData ciShellCIBridgeData = (CIShellCIBridgeData) data;
            assertNotNull(ciShellCIBridgeData.getCIShellData());
            org.cishell.framework.data.Data ciShellData = ciShellCIBridgeData.getCIShellData();
            assertEquals(label, ciShellData.getMetadata().get(LABEL));
            assertEquals(name, ciShellData.getMetadata().get("Name"));
            assertEquals(((CIShellCIBridgeData) parentData).getCIShellData(), ciShellData.getMetadata().get(PARENT));
            assertEquals(dataType.name(), ciShellData.getMetadata().get(TYPE));
            assertEquals("SomeValue", ciShellData.getMetadata().get("CustomProperty"));
        }

        //update custom property. this should not add a new property entry with a new value but should update the value
        //against the already present key
        PropertyInput sameCustomPropertyWithDiffValue = new PropertyInput("CustomProperty", "SomeOtherValue");
        DataProperties newDataProperties = new DataProperties();
        newDataProperties.setOtherProperties(Collections.singletonList(sameCustomPropertyWithDiffValue));

        assertTrue(cishellCIBridgeDataFacade.updateData(data.getId(), newDataProperties));
        assertEquals(1, data.getOtherProperties().size());
        assertEquals(sameCustomPropertyWithDiffValue.getKey(), data.getOtherProperties().get(0).getKey());
        assertEquals(sameCustomPropertyWithDiffValue.getValue(), data.getOtherProperties().get(0).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNonExistentData() {
        cishellCIBridgeDataFacade.updateData("someRandomID", new DataProperties());
    }

    @Test
    public void downloadFile() throws IOException {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        assertEquals(new File(dataFileUrl.getFile()).getCanonicalPath(), cishellCIBridgeDataFacade.downloadData(data.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void downloadNonExistentData() {
        cishellCIBridgeDataFacade.downloadData("someRandomID");
    }

    @Test
    public void getDataWithEmptyFilter() {
        DataQueryResults queryResults;

        //test with empty filter and no data
        queryResults = cishellCIBridgeDataFacade.getData(new DataFilter());
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(0, queryResults.getResults().size());

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        //test with empty filter
        queryResults = cishellCIBridgeDataFacade.getData(new DataFilter());
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
    }

    @Test
    public void getDataWithSpecifiedPagination() {

    }

    @Test
    public void getDataWithSpecifiedFormats() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        DataProperties dataProperties1 = new DataProperties();
        String format1 = "file:text/plain";
        dataProperties1.setFormat(format1);

        DataProperties dataProperties2 = new DataProperties();
        String format2 = "file:text/csv";
        dataProperties2.setFormat(format2);

        //upload 2 data object with different format
        Data txtData1 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties2);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //querying on first format should return only 1 result
        filter.setFormats(Collections.singletonList(format1));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());

        //querying on both the formats should return both data
        filter.setFormats(Arrays.asList(format1, format2));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());

        //querying on any other format should return nothing
        filter.setFormats(Collections.singletonList("SomeNonExistentDataFormat"));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());

    }

    @Test
    public void getDataWithSpecifiedType() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        DataProperties dataProperties1 = new DataProperties();
        dataProperties1.setType(DataType.TEXT);

        DataProperties dataProperties2 = new DataProperties();
        dataProperties2.setType(DataType.NETWORK);

        Data txtData1 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties2);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //query for the one data type should return one result
        filter.setTypes(Collections.singletonList(DataType.TEXT));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());

        //query for either of the two data type should return both results
        filter.setTypes(Arrays.asList(DataType.TEXT, DataType.NETWORK));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());

        //query for any other data type should return nothing
        filter.setTypes(Collections.singletonList(DataType.PLOT));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getDataWithSpecifiedIds() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        Data txtData1 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData2 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //querying for either of two data id should return two results
        filter.setDataIds(Arrays.asList(txtData1.getId(), txtData3.getId()));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        //querying for non-existent data id should not return anything
        filter.setDataIds(Collections.singletonList("someNonExistentID"));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getModifiedOrUnmodifiedData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        Data txtData1 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData2 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData4 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        txtData2.setModified(false);
        txtData4.setModified(false);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        filter.setModified(true);
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        filter.setModified(false);
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData2.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData4.getId(), queryResults.getResults().get(1).getId());
    }

    @Test
    public void getDataWithSpecifiedCustomProperties() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        PropertyInput propertyInput = new PropertyInput("PropertyKey1", "PropertyValue1");
        PropertyInput propertyInput2 = new PropertyInput("PropertyKey2", "PropertyValue2");
        PropertyInput propertyInput3 = new PropertyInput("PropertyKey2", "PropertyValue3");

        DataProperties dataProperties1 = new DataProperties();
        dataProperties1.setOtherProperties(Collections.singletonList(propertyInput));
        DataProperties dataProperties2 = new DataProperties();
        dataProperties2.setOtherProperties(Arrays.asList(propertyInput, propertyInput2));
        DataProperties dataProperties3 = new DataProperties();
        dataProperties3.setOtherProperties(Collections.singletonList(propertyInput3));

        Data txtData1 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties2);
        Data txtData3 = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties3);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //query to match first property should return both results
        filter.setProperties(Collections.singletonList(propertyInput));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData2.getId(), queryResults.getResults().get(1).getId());

        //query to match both properties should return second result
        filter.setProperties(Arrays.asList(propertyInput, propertyInput2));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals(txtData2.getId(), queryResults.getResults().get(0).getId());

        //query to match either of the the value for a property should return required result
        filter.setProperties(Arrays.asList(propertyInput2, propertyInput3));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData2.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        //query to check if the data has multiple properties when they have at least one of those
        filter.setProperties(Arrays.asList(propertyInput, propertyInput3));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());

        //if matched on non-existent property key, should not return anything
        filter.setProperties(Collections.singletonList(new PropertyInput("someNonExistentPropertyKey", "Its Value")));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());

        //if matched on non-existent property value, should not return anything
        filter.setProperties(Collections.singletonList(new PropertyInput("CustomKey", "someNonExistentPropertyValue")));
        queryResults = cishellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void validateData() {

    }

    @Test
    public void findConverters() {

    }

    @Test
    public void findConvertersByFormat() {
        String inputDataFormat = "file:text/A";
        String outputDataFormat = "file:text/C";

        cishellCIBridgeDataFacade.findConvertersByFormat(inputDataFormat, outputDataFormat);
    }

    @Test
    public void dataAddedSubscriptionTests() {
        //Setting up a mock Subscriber
        TestSubscriber<Data> testSubscriber = new TestSubscriber<>();
        cishellCIBridgeDataFacade.dataAdded().subscribe(testSubscriber);

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
        DataType dataType = DataType.DATABASE;
        PropertyInput customProperty = new PropertyInput("CustomProperty", "SomeValue");
        DataProperties dataProperties = new DataProperties();
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setOtherProperties(Collections.singletonList(customProperty));

        Data expectedData = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties);

        // Wait till the subscriber collects 1 onNext values
        testSubscriber.awaitCount(1);

        testSubscriber.assertNoErrors();
        // Getting values from subscriber
        List<Data> resultData = testSubscriber.values();
        assertTrue(resultData.size() == 1);

        // Assert the log messages with the expected results
        Data actualData = resultData.get(0);

        assertSame(expectedData.getId(), actualData.getId());
        assertSame(expectedData.getFormat(), actualData.getFormat());
        assertSame(expectedData.getLabel(), actualData.getLabel());
        assertSame(expectedData.getModified(), actualData.getModified());
        assertSame(expectedData.getType(), actualData.getType());
        assertSame(expectedData.getOtherProperties().get(0).getKey(), actualData.getOtherProperties().get(0).getKey());
        assertSame(expectedData.getOtherProperties().get(0).getValue(), actualData.getOtherProperties().get(0).getValue());

    }

    @Test
    public void dataRemovedSubscriptionTests() {
        //Setting up a mock Subscriber
        TestSubscriber<Data> testSubscriber = new TestSubscriber<>();
        cishellCIBridgeDataFacade.dataRemoved().subscribe(testSubscriber);

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
        DataType dataType = DataType.DATABASE;
        PropertyInput customProperty = new PropertyInput("CustomProperty", "SomeValue");
        DataProperties dataProperties = new DataProperties();
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setOtherProperties(Collections.singletonList(customProperty));

        Data uploadedData = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties);

        // Removing the data that is added
        cishellCIBridgeDataFacade.removeData(uploadedData.getId());

        // Wait till the subscriber collects 1 onNext values
        testSubscriber.awaitCount(1);

        testSubscriber.assertNoErrors();
        // Getting values from subscriber
        List<Data> resultData = testSubscriber.values();
        assertTrue(resultData.size() == 1);

        // Assert the log messages with the expected results
        Data actualData = resultData.get(0);

        assertSame(uploadedData.getId(), actualData.getId());
        assertSame(uploadedData.getFormat(), actualData.getFormat());
        assertSame(uploadedData.getLabel(), actualData.getLabel());
        assertSame(uploadedData.getModified(), actualData.getModified());
        assertSame(uploadedData.getType(), actualData.getType());
        assertSame(uploadedData.getOtherProperties().get(0).getKey(), actualData.getOtherProperties().get(0).getKey());
        assertSame(uploadedData.getOtherProperties().get(0).getValue(), actualData.getOtherProperties().get(0).getValue());
    }

    @Test
    public void dataUpdatedSubscriptionTests() {

        //Setting up a mock Subscriber
        TestSubscriber<Data> testSubscriber = new TestSubscriber<>();
        cishellCIBridgeDataFacade.dataUpdated().subscribe(testSubscriber);

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
        DataType dataType = DataType.DATABASE;
        PropertyInput customProperty = new PropertyInput("CustomProperty", "SomeValue");
        DataProperties dataProperties = new DataProperties();
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setOtherProperties(Collections.singletonList(customProperty));

        Data uploadedData = cishellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties);

        // Updating the data properties that is added
        dataProperties.setLabel("New Research Papers");
        dataProperties.setType(DataType.MODEL);
        dataProperties.setName("Aravind Bharatha");

        Boolean statusOfUpdate = cishellCIBridgeDataFacade.updateData(uploadedData.getId(), dataProperties);

        assertTrue(statusOfUpdate);

        // Wait till the subscriber collects 1 onNext values
        testSubscriber.awaitCount(1);

        testSubscriber.assertNoErrors();
        // Getting values from subscriber
        List<Data> resultData = testSubscriber.values();

        assertTrue(resultData.size() == 1);

        // Assert the log messages with the expected results
        Data actualData = resultData.get(0);

        assertSame(uploadedData.getId(), actualData.getId());
        assertSame(uploadedData.getFormat(), actualData.getFormat());
        assertSame(uploadedData.getLabel(), actualData.getLabel());
        assertSame(uploadedData.getModified(), actualData.getModified());
        assertSame(uploadedData.getType(), actualData.getType());
        assertSame(uploadedData.getOtherProperties().get(0).getKey(), actualData.getOtherProperties().get(0).getKey());
        assertSame(uploadedData.getOtherProperties().get(0).getValue(), actualData.getOtherProperties().get(0).getValue());

    }

    @After
    public void tearDown() {

        for (org.cishell.framework.data.Data data : getCIShellCIBridge().getDataManagerService().getAllData()) {
            getCIShellCIBridge().getDataManagerService().removeData(data);
        }

        assertEquals(0, getDataManagerService().getAllData().length);
        assertEquals(0, cishellCIBridgeDataFacade.getCIBridgeDataMap().size());
        assertEquals(0, cishellCIBridgeDataFacade.getCIShellDataCIBridgeDataMap().size());
    }
}