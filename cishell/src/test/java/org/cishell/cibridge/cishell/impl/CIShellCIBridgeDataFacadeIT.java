package org.cishell.cibridge.cishell.impl;

import org.cishell.cibridge.cishell.IntegrationTestCase;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataProperties;
import org.cishell.cibridge.core.model.DataType;
import org.cishell.cibridge.core.model.PropertyInput;
import org.junit.Test;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CIShellCIBridgeDataFacadeIT extends IntegrationTestCase {

    @Test
    public void uploadData() {
        URL dataFileUrl = getClass().getClassLoader().getResource("sample.txt");
        assertNotNull(dataFileUrl);
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), null);
        assertNotNull(data);
        assertNotNull(data.getId());
        assertNotNull(data.getFormat());
        assertNotNull(data.getCIShellData());
        assertEquals("file-ext:txt", data.getFormat());
        assertEquals("file-ext:txt", data.getCIShellData().getFormat());

        assertEquals(data.getCIShellData(), getDataManagerService().getAllData()[0]);

        getDataManagerService().removeData(getDataManagerService().getAllData()[0]);
        assertEquals(0, getDataManagerService().getAllData().length);
    }

    @Test
    public void uploadDataWithProperties() {
        String format = "file:text/csv";
        String label = "Research papers";
        String name = "Research papers by Laszlo Barabasi";
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
        dataProperties.setOtherProperties(otherProperties);
        Data data = getCIShellCIBridge().cishellData.uploadData(dataFileUrl.getFile(), dataProperties);
        System.out.println(data);

        assertNotNull(data);
        assertNotNull(data.getId());
        assertNotNull(data.getFormat());
        assertNotNull(data.getCIShellData());
        assertEquals(format, data.getFormat());
        assertEquals(label, data.getLabel());
        assertEquals(name, data.getName());
        assertEquals(dataType, data.getType());
        assertEquals(customProperty.getKey(), data.getOtherProperties().get(0).getKey());

        assertEquals(format, data.getCIShellData().getFormat());
        //todo check properties in cishell data

        assertEquals(data.getCIShellData(), getDataManagerService().getAllData()[0]);

        getDataManagerService().removeData(getDataManagerService().getAllData()[0]);
        assertEquals(0, getDataManagerService().getAllData().length);

        IntegrationTestCase integrationTestCase = new IntegrationTestCase() {
        };
    }
}