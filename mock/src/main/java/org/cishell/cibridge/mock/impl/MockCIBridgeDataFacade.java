package org.cishell.cibridge.mock.impl;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.*;

import java.util.List;

public class MockCIBridgeDataFacade implements CIBridge.DataFacade {

	@Override
	public String validateData(String algorithmDefinitionId, List<String> dataIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AlgorithmInstance> findConverters(String dataId, String outFormat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataQueryResults getData(DataFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadData(String dataId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data uploadData(String file, DataProperties properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean removeData(String dataId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateData(String dataId, DataProperties properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data dataAdded() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data dataRemoved() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data dataUpdated() {
		// TODO Auto-generated method stub
		return null;
	}

}
