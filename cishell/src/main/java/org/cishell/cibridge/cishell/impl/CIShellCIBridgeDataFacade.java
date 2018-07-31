package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataFilter;
import org.cishell.cibridge.core.model.DataProperties;
import org.cishell.cibridge.core.model.DataQueryResults;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;


public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {
	private CIShellCIBridge cibridge;
	
	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@Override
	public String validateData(String algorithmDefinitionId, List<String> dataIds) {
		// TODO Auto-generated method stub
		// check the dataIds validates with top level algorithmDef
		// not compatible, ret false
		
		// AlgorithmDefinitionQueryResults
		List<String> defIDs = new ArrayList<String>();
		defIDs.add(algorithmDefinitionId);
		AlgorithmFilter algorithmFilter = new AlgorithmFilter(defIDs, null, null, null, null, null, null, 0, 0);
		AlgorithmDefinitionQueryResults algorithmDefinitionQueryResults = this.cibridge.cishellAlgorithm.getAlgorithmDefinitions(algorithmFilter);
		AlgorithmDefinition algDefinition = (AlgorithmDefinition) algorithmDefinitionQueryResults.getResults().get(0);
		AlgorithmFactory algorithm = this.cibridge.getAlgorithmFactory(algorithmDefinitionId);
		
		DataFilter dataFilter = new DataFilter(dataIds, null, false, null, null);
		DataQueryResults dataQueryResults = getData(dataFilter);
		List<Data> data = dataQueryResults.getResults();
		int counter = 0;
		for (String algInData : algDefinition.getInData()) {
			if (!(algInData.equalsIgnoreCase(data.get(counter).getFormat()))){
				return "data formats are not compatible";
			}
			counter++;
		}
		
		if (algorithm instanceof DataValidator) {
			org.cishell.framework.data.Data[] realData = this.getRealData(dataIds);
			if (realData == null) {
				return "data is missing";
			} else {
				return ((DataValidator) algorithm).validate(realData);
			}
		}
	
		return "";
	}
	
	private org.cishell.framework.data.Data[] getRealData(List<String> dataIds) {
		DataManagerService dataManager = cibridge.getDataManagerService();
		
		Map<String, org.cishell.framework.data.Data> dataMapping = new HashMap<String, org.cishell.framework.data.Data>();
		for (org.cishell.framework.data.Data data : dataManager.getAllData()) {
			dataMapping.put((String) data.getMetadata().get("cibridge.data.id"), data);
		}
		
		List<org.cishell.framework.data.Data> results = new ArrayList<org.cishell.framework.data.Data>();
		for (String dataId : dataIds) {
			if (dataMapping.containsKey(dataId)) {
				results.add(dataMapping.get(dataId));
			} else {
				// results.add(null);
				return null;
			}
		}
		return (org.cishell.framework.data.Data[]) results.toArray();
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
