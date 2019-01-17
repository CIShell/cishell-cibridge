package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CIShellCIBridgeDataFacadeIT extends IntegrationTestCase {

    private CIShellCIBridgeDataFacade ciShellCIBridgeDataFacade = getCIShellCIBridge().cishellData;

    @Test(expected = IllegalArgumentException.class)
    public void uploadNonExistentFile() {
        ciShellCIBridgeDataFacade.uploadData("SomeNonExistentFile.txt", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void uploadDirectory() {
        ciShellCIBridgeDataFacade.uploadData(System.getProperty("user.dir"), null);
    }

    @Test
    public void uploadFileWithoutProperties() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

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
        String format = "file:text/csv";
        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
        String parentDataId = "someParentId";
        DataType dataType = DataType.DATABASE;
        PropertyInput customProperty = new PropertyInput("CustomProperty", "SomeValue");

        URL dataFileUrl = getClass().getClassLoader().getResource("LaszloBarabasi.csv");
        assertNotNull(dataFileUrl);
        DataProperties dataProperties = new DataProperties();
        dataProperties.setFormat(format);
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setParent(parentDataId);
        dataProperties.setOtherProperties(Collections.singletonList(customProperty));
        Data data = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties);

        assertNotNull(data);
        assertNotNull(data.getId());
        assertNotNull(data.getFormat());

        assertEquals(format, data.getFormat());
        assertEquals(label, data.getLabel());
        assertEquals(name, data.getName());
        assertEquals(dataType, data.getType());
        assertEquals(parentDataId, data.getParentDataId());
        assertEquals(customProperty.getKey(), data.getOtherProperties().get(0).getKey());
        assertEquals(customProperty.getValue(), data.getOtherProperties().get(0).getValue());

        if (data instanceof CIShellCIBridgeData) {
            CIShellCIBridgeData ciShellCIBridgeData = (CIShellCIBridgeData) data;
            assertNotNull(ciShellCIBridgeData.getCIShellData());
            org.cishell.framework.data.Data ciShellData = ciShellCIBridgeData.getCIShellData();
            assertEquals(format, ciShellData.getFormat());
            assertEquals(label, ciShellData.getMetadata().get("Label"));
            assertEquals(name, ciShellData.getMetadata().get("Name"));
            assertEquals(dataType.name(), ciShellData.getMetadata().get("Type"));
            assertEquals("SomeValue", ciShellData.getMetadata().get("CustomProperty"));
            assertEquals(ciShellCIBridgeData.getCIShellData(), getDataManagerService().getAllData()[0]);
        }

    }

    @Test
    public void removeData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        assertTrue(ciShellCIBridgeDataFacade.removeData(data.getId()));
        assertEquals(0, getDataManagerService().getAllData().length);

        assertFalse(ciShellCIBridgeDataFacade.removeData("SomeNonExistentId"));
    }

    @Test
    public void updateData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
        DataType dataType = DataType.DATABASE;
        String parentDataId = "someParentId";
        PropertyInput customProperty = new PropertyInput("CustomProperty", "SomeValue");

        DataProperties dataProperties = new DataProperties();
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setParent(parentDataId);
        dataProperties.setOtherProperties(Collections.singletonList(customProperty));
        assertTrue(ciShellCIBridgeDataFacade.updateData(data.getId(), dataProperties));

        assertEquals(label, data.getLabel());
        assertEquals(name, data.getName());
        assertEquals(dataType, data.getType());
        assertEquals(parentDataId, data.getParentDataId());
        assertEquals(1, data.getOtherProperties().size());
        assertEquals(customProperty.getKey(), data.getOtherProperties().get(0).getKey());
        assertEquals(customProperty.getValue(), data.getOtherProperties().get(0).getValue());

        if (data instanceof CIShellCIBridgeData) {
            CIShellCIBridgeData ciShellCIBridgeData = (CIShellCIBridgeData) data;
            assertNotNull(ciShellCIBridgeData.getCIShellData());
            org.cishell.framework.data.Data ciShellData = ciShellCIBridgeData.getCIShellData();
            assertEquals(label, ciShellData.getMetadata().get("Label"));
            assertEquals(name, ciShellData.getMetadata().get("Name"));
            assertEquals(dataType.name(), ciShellData.getMetadata().get("Type"));
            assertEquals("SomeValue", ciShellData.getMetadata().get("CustomProperty"));
        }

        //update custom property. this should not add a new property entry with a new value but should update the value
        //against the already present key
        PropertyInput sameCustomPropertyWithDiffValue = new PropertyInput("CustomProperty", "SomeOtherValue");
        DataProperties newDataProperties = new DataProperties();
        newDataProperties.setOtherProperties(Collections.singletonList(sameCustomPropertyWithDiffValue));

        assertTrue(ciShellCIBridgeDataFacade.updateData(data.getId(), newDataProperties));
        assertEquals(1, data.getOtherProperties().size());
        assertEquals(sameCustomPropertyWithDiffValue.getKey(), data.getOtherProperties().get(0).getKey());
        assertEquals(sameCustomPropertyWithDiffValue.getValue(), data.getOtherProperties().get(0).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNonExistentData() {
        ciShellCIBridgeDataFacade.updateData("someRandomID", new DataProperties());
    }

    @Test
    public void downloadFile() throws IOException {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        assertEquals(new File(dataFileUrl.getFile()).getCanonicalPath(), ciShellCIBridgeDataFacade.downloadData(data.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void downloadNonExistentData() {
        ciShellCIBridgeDataFacade.downloadData("someRandomID");
    }

    @Test
    public void getDataWithEmptyFilter() {
        DataQueryResults queryResults;

        //test with empty filter and no data
        queryResults = ciShellCIBridgeDataFacade.getData(new DataFilter());
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(0, queryResults.getResults().size());

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        //test with empty filter
        queryResults = ciShellCIBridgeDataFacade.getData(new DataFilter());
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
    }

    @Test
    public void getDataWithSpecifiedPagination() {

        DataFilter filter;
        DataQueryResults queryResults;

        filter = new DataFilter();
        filter.setFormats(Collections.singletonList("file-ext:txt"));
        filter.setLimit(3);
        filter.setOffset(0);

        //get results for empty data cache
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(0, queryResults.getResults().size());

        //upload data
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data txtData1 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData2 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData4 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData5 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData6 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData7 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData8 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        //limit:3 offset:0
        //first page
        filter.setLimit(3);
        filter.setOffset(0);

        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertTrue(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData2.getId(), queryResults.getResults().get(1).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(2).getId());

        //limit:3 offset:3
        //second page
        filter.setOffset(3);
        filter.setLimit(3);
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertTrue(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
        assertEquals(txtData4.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData5.getId(), queryResults.getResults().get(1).getId());
        assertEquals(txtData6.getId(), queryResults.getResults().get(2).getId());

        //limit:3 offset:6
        //third and the last page
        filter.setLimit(3);
        filter.setOffset(6);
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData7.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData8.getId(), queryResults.getResults().get(1).getId());

        //limit:9 offset:0
        //should return everything
        filter.setLimit(9);
        filter.setOffset(0);
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(8, queryResults.getResults().size());

        //limit:9 offset:9
        //should not return anything
        filter.setLimit(9);
        filter.setOffset(9);
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(0, queryResults.getResults().size());
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
        Data txtData1 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties2);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //querying on first format should return only 1 result
        filter.setFormats(Collections.singletonList(format1));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());

        //querying on both the formats should return both data
        filter.setFormats(Arrays.asList(format1, format2));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());

        //querying on any other format should return nothing
        filter.setFormats(Collections.singletonList("SomeNonExistentDataFormat"));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());

    }

    @Test
    public void getDataWithSpecifiedType() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        DataProperties dataProperties1 = new DataProperties();
        dataProperties1.setType(DataType.TEXT);

        DataProperties dataProperties2 = new DataProperties();
        dataProperties2.setType(DataType.UNKNOWN);

        Data txtData1 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties2);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //query for the one data type should return one result
        filter.setTypes(Collections.singletonList(DataType.TEXT));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());

        //query for either of the two data type should return both results
        filter.setTypes(Arrays.asList(DataType.TEXT, DataType.UNKNOWN));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());

        //query for any other data type should return nothing
        filter.setTypes(Collections.singletonList(DataType.PLOT));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getDataWithSpecifiedIds() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        Data txtData1 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData2 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //querying for either of two data id should return two results
        filter.setDataIds(Arrays.asList(txtData1.getId(), txtData3.getId()));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        //querying for non-existent data id should not return anything
        filter.setDataIds(Collections.singletonList("someNonExistentID"));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void getModifiedOrUnmodifiedData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        Data txtData1 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        txtData1.setModified(true);
        Data txtData2 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);
        txtData3.setModified(true);
        Data txtData4 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), null);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        filter.setModified(true);
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        filter.setModified(false);
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
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

        Data txtData1 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties2);
        Data txtData3 = ciShellCIBridgeDataFacade.uploadData(dataFileUrl.getFile(), dataProperties3);

        DataFilter filter = new DataFilter();
        DataQueryResults queryResults;

        //query to match first property should return both results
        filter.setProperties(Collections.singletonList(propertyInput));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData2.getId(), queryResults.getResults().get(1).getId());

        //query to match both properties should return second result
        filter.setProperties(Arrays.asList(propertyInput, propertyInput2));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals(txtData2.getId(), queryResults.getResults().get(0).getId());

        //query to match either of the the value for a property should return required result
        filter.setProperties(Arrays.asList(propertyInput2, propertyInput3));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData2.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        //query to check if the data has multiple properties when they have at least one of those
        filter.setProperties(Arrays.asList(propertyInput, propertyInput3));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());

        //if matched on non-existent property key, should not return anything
        filter.setProperties(Collections.singletonList(new PropertyInput("someNonExistentPropertyKey", "Its Value")));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());

        //if matched on non-existent property value, should not return anything
        filter.setProperties(Collections.singletonList(new PropertyInput("CustomKey", "someNonExistentPropertyValue")));
        queryResults = ciShellCIBridgeDataFacade.getData(filter);
        assertEquals(0, queryResults.getResults().size());
    }

    @Test
    public void validateData() {

    }

    @After
    public void tearDown() {
        List<String> dataIdList = new LinkedList<>(ciShellCIBridgeDataFacade.getDataCache().keySet());
        for (String dataId : dataIdList) {
            ciShellCIBridgeDataFacade.removeData(dataId);
        }
        assertEquals(0, getDataManagerService().getAllData().length);
        assertEquals(0, ciShellCIBridgeDataFacade.getDataCache().size());
    }
}