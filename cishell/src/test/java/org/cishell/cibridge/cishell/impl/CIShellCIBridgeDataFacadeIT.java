package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.junit.After;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CIShellCIBridgeDataFacadeIT extends IntegrationTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void uploadNonExistentFile() {
        getCIShellCIBridge().cishellData.uploadData("SomeNonExistentFile.txt", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void uploadDirectory() {
        getCIShellCIBridge().cishellData.uploadData(System.getProperty("user.dir"), null);
    }

    @Test
    public void uploadFileWithoutProperties() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);

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
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties);

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
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);

        boolean isSuccess = getCIShellCIBridge().cishellData.removeData(data.getId());
        assertTrue(isSuccess);
        assertEquals(0, getDataManagerService().getAllData().length);

        boolean isSuccess1 = getCIShellCIBridge().cishellData.removeData("SomeNonExistentId");
        assertFalse(isSuccess1);
    }

    @Test
    public void updateData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);

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
        Boolean success = getCIShellCIBridge().cishellData.updateData(data.getId(), dataProperties);
        assertTrue(success);

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

        assertTrue(getCIShellCIBridge().cishellData.updateData(data.getId(), newDataProperties));
        assertEquals(1, data.getOtherProperties().size());
        assertEquals(sameCustomPropertyWithDiffValue.getKey(), data.getOtherProperties().get(0).getKey());
        assertEquals(sameCustomPropertyWithDiffValue.getValue(), data.getOtherProperties().get(0).getValue());

        //todo add test cases for failure
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNonExistentData() {
        getCIShellCIBridge().cishellData.updateData("someRandomID", new DataProperties());
    }

    @Test
    public void downloadFile() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        assertEquals(dataFileUrl.getFile(), getCIShellCIBridge().cishellData.downloadData(data.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void downloadNonExistentData() {
        getCIShellCIBridge().cishellData.downloadData("someRandomID");
    }


    @Test
    public void getDataWithSpecifiedPagination() {

        DataFilter filter = new DataFilter();
        filter.setFormats(Collections.singletonList("file-ext:txt"));
        filter.setLimit(3);
        filter.setOffset(0);

        DataQueryResults queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(0, queryResults.getResults().size());

        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data txtData1 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData2 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData4 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData5 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData6 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData7 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData8 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);

        filter.setLimit(3);
        filter.setOffset(0);

        queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertTrue(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData2.getId(), queryResults.getResults().get(1).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(2).getId());

        filter.setOffset(3);
        filter.setLimit(3);
        queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertTrue(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(3, queryResults.getResults().size());
        assertEquals(txtData4.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData5.getId(), queryResults.getResults().get(1).getId());
        assertEquals(txtData6.getId(), queryResults.getResults().get(2).getId());

        filter.setLimit(3);
        filter.setOffset(6);
        queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertTrue(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData7.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData8.getId(), queryResults.getResults().get(1).getId());

        filter.setLimit(9);
        filter.setOffset(0);
        queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertFalse(queryResults.getPageInfo().hasNextPage());
        assertFalse(queryResults.getPageInfo().hasPreviousPage());
        assertEquals(8, queryResults.getResults().size());

        filter.setLimit(9);
        filter.setOffset(9);
        queryResults = getCIShellCIBridge().cishellData.getData(filter);
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
        String format2 = "file-ext:txt";
        dataProperties2.setFormat(format2);

        Data txtData1 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties2);

        DataFilter filter = new DataFilter();

        filter.setFormats(Collections.singletonList(format1));
        DataQueryResults queryResults1 = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(1, queryResults1.getResults().size());
        assertEquals(txtData1.getId(), queryResults1.getResults().get(0).getId());

        filter.setFormats(Arrays.asList(format1, format2));
        DataQueryResults queryResults2 = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(2, queryResults2.getResults().size());

    }

    @Test
    public void getDataWithSpecifiedType() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        DataProperties dataProperties1 = new DataProperties();
        dataProperties1.setType(DataType.TEXT);

        DataProperties dataProperties2 = new DataProperties();
        dataProperties2.setType(DataType.UNKNOWN);

        Data txtData1 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties2);

        DataFilter filter = new DataFilter();

        filter.setTypes(Collections.singletonList(DataType.TEXT));
        DataQueryResults queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(1, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());

        filter.setTypes(Arrays.asList(DataType.TEXT, DataType.UNKNOWN));
        DataQueryResults queryResults2 = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(2, queryResults2.getResults().size());
    }

    @Test
    public void getDataWithSpecifiedIds() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        Data txtData1 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData2 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);

        DataFilter filter = new DataFilter();
        filter.setDataIds(Arrays.asList(txtData1.getId(), txtData3.getId()));

        DataQueryResults queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());
    }

    @Test
    public void getModifiedOrUnmodifiedData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        Data txtData1 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        txtData1.setModified(true);
        Data txtData2 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        txtData3.setModified(true);
        Data txtData4 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);

        DataFilter filter = new DataFilter();
        filter.setModified(true);

        DataQueryResults queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        filter.setModified(false);
        DataQueryResults queryResults2 = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(2, queryResults2.getResults().size());
        assertEquals(txtData2.getId(), queryResults2.getResults().get(0).getId());
        assertEquals(txtData4.getId(), queryResults2.getResults().get(1).getId());
    }

    @Test
    public void getDataWithSpecifiedCustomProperties() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);

        PropertyInput propertyInput = new PropertyInput("CustomKey", "CustomValue");
        PropertyInput propertyInput2 = new PropertyInput("CustomKey2", "CustomValue2");

        DataProperties dataProperties1 = new DataProperties();
        dataProperties1.setOtherProperties(Collections.singletonList(propertyInput));
        DataProperties dataProperties2 = new DataProperties();
        dataProperties2.setOtherProperties(Arrays.asList(propertyInput, propertyInput2));

        Data txtData1 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties1);
        Data txtData2 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        Data txtData3 = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties2);

        DataFilter filter = new DataFilter();
        filter.setProperties(Collections.singletonList(propertyInput));
        DataQueryResults queryResults = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(2, queryResults.getResults().size());
        assertEquals(txtData1.getId(), queryResults.getResults().get(0).getId());
        assertEquals(txtData3.getId(), queryResults.getResults().get(1).getId());

        //todo this predicate is OR or AND of the individual criteria
        filter.setProperties(Arrays.asList(propertyInput, propertyInput2));
        DataQueryResults queryResults2 = getCIShellCIBridge().cishellData.getData(filter);
        assertEquals(1, queryResults2.getResults().size());
        assertEquals(txtData3.getId(), queryResults2.getResults().get(0).getId());
    }

    @After
    public void tearDown() {
        List<String> dataIdList = new LinkedList<>(getCIShellCIBridge().cishellData.getDataCache().keySet());
        for (String dataId : dataIdList) {
            getCIShellCIBridge().cishellData.removeData(dataId);
        }
        assertEquals(0, getDataManagerService().getAllData().length);
        assertEquals(0, getCIShellCIBridge().cishellData.getDataCache().size());
    }
}