package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataProperties;
import org.cishell.cibridge.core.model.DataType;
import org.cishell.cibridge.core.model.PropertyInput;
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
        assertEquals(customProperty.getKey(), data.getOtherProperties().get(0).getKey());

        if (data instanceof CIShellCIBridgeData) {
            CIShellCIBridgeData ciShellCIBridgeData = (CIShellCIBridgeData) data;
            assertNotNull(ciShellCIBridgeData.getCIShellData());
            org.cishell.framework.data.Data ciShellData = ciShellCIBridgeData.getCIShellData();
            assertEquals(label, ciShellData.getMetadata().get("Label"));
            assertEquals(name, ciShellData.getMetadata().get("Name"));
            assertEquals(dataType.name(), ciShellData.getMetadata().get("Type"));
            assertEquals("SomeValue", ciShellData.getMetadata().get("CustomProperty"));
        }

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


    @After
    public void tearDown() {
        if (getDataManagerService().getAllData() != null && getDataManagerService().getAllData().length > 0) {
            for (org.cishell.framework.data.Data data : getDataManagerService().getAllData()) {
                getDataManagerService().removeData(data);
            }
        }
        assertEquals(0, getDataManagerService().getAllData().length);
    }
}