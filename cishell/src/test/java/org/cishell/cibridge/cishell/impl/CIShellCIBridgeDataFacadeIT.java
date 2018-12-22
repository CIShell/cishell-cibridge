package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.*;
import org.junit.After;
import org.junit.Test;

import java.net.URL;
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
    public void uploadFile() {
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
        List<PropertyInput> otherProperties = new LinkedList<>();
        otherProperties.add(customProperty);

        URL dataFileUrl = getClass().getClassLoader().getResource("LaszloBarabasi.csv");
        assertNotNull(dataFileUrl);
        DataProperties dataProperties = new DataProperties();
        dataProperties.setFormat(format);
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setParent(parentDataId);
        dataProperties.setOtherProperties(otherProperties);
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
        List<PropertyInput> otherProperties = new LinkedList<>();
        otherProperties.add(customProperty);

        DataProperties dataProperties = new DataProperties();
        dataProperties.setLabel(label);
        dataProperties.setName(name);
        dataProperties.setType(dataType);
        dataProperties.setParent(parentDataId);
        dataProperties.setOtherProperties(otherProperties);
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
        List<PropertyInput> newOtherProperties = new LinkedList<>();
        newOtherProperties.add(sameCustomPropertyWithDiffValue);
        DataProperties newDataProperties = new DataProperties();
        newDataProperties.setOtherProperties(newOtherProperties);

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
    public void getDataWithPagination() {

        DataFilter filter = new DataFilter();
        List<String> formats = new LinkedList<>();
        formats.add("file-ext:txt");
        filter.setFormats(formats);
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