package org.cishell.cibridge.cishell.impl;

import java.util.ArrayList;
import java.util.List;

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
import org.cishell.app.service.datamanager.DataManagerService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {
	private final BundleContext context;
	private CIShellCIBridge cibridge;

	public CIShellCIBridgeDataFacade(BundleContext context) {
		this.context = context;
	}
	
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
		AlgorithmFactory algorithm = this.cibridge.cishellAlgorithm.getAlgorithmFactory(algorithmDefinitionId);
		
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
			List<org.cishell.framework.data.Data> realData = this.getRealData(dataIds);
			return ((DataValidator) algorithm).validate((org.cishell.framework.data.Data[]) realData.toArray());
		}
		//datamanger of cishell data
		//	
		
		return "";
	}
	
	private List<org.cishell.framework.data.Data> getRealData(List<String> dataIds) {
		// TODO Auto-generated method stub
		DataManagerService dataManager = (DataManagerService) this.getService(DataManagerService.class);
		List<org.cishell.framework.data.Data> results = new ArrayList();
		for (org.cishell.framework.data.Data data : dataManager.getAllData()) {
			if (dataIds.indexOf(data.getMetadata().get("cibridge.data.id")) != -1) {
				results.add(data);
			}
		}
		
		return results;
	}

	@SuppressWarnings("rawtypes")
	public Object getService(Class c){
		ServiceReference ref = context.getServiceReference(c.getName());
		return ref != null ? context.getService(ref) : null;
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
