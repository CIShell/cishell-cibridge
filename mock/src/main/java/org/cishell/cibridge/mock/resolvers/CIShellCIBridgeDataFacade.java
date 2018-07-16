package org.cishell.cibridge.mock.resolvers;

import java.util.List;

import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.DataFilter;
import org.cishell.cibridge.core.model.DataQueryResults;
import org.osgi.framework.BundleContext;

public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {
	
	private final BundleContext context;
	public CIShellCIBridgeDataFacade(BundleContext context) {
		this.context=context;
	}

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

}
