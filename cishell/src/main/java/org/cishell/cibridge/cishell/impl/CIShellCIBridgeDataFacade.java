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
import org.cishell.service.conversion.Converter;
import org.cishell.service.conversion.DataConversionService;

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
		AlgorithmDefinitionQueryResults algorithmDefinitionQueryResults = this.cibridge.cishellAlgorithm
				.getAlgorithmDefinitions(algorithmFilter);
		AlgorithmDefinition algDefinition = (AlgorithmDefinition) algorithmDefinitionQueryResults.getResults().get(0);
		AlgorithmFactory algorithm = this.cibridge.cishellAlgorithm.getAlgorithmFactory(algorithmDefinitionId);

		DataFilter dataFilter = new DataFilter(dataIds, null, false, null, null);
		DataQueryResults dataQueryResults = getData(dataFilter);
		List<Data> data = dataQueryResults.getResults();
		int counter = 0;
		for (String algInData : algDefinition.getInData()) {
			if (!(algInData.equalsIgnoreCase(data.get(counter).getFormat()))) {
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

	// get dataMap
	private Map<String, org.cishell.framework.data.Data> getDataMap() {
		DataManagerService dataManager = cibridge.getDataManagerService();

		Map<String, org.cishell.framework.data.Data> dataMapping = new HashMap<String, org.cishell.framework.data.Data>();
		for (org.cishell.framework.data.Data data : dataManager.getAllData()) {
			dataMapping.put((String) data.getMetadata().get("cibridge.data.id"), data);
		}
		return dataMapping;
	}

	private org.cishell.framework.data.Data[] getRealData(List<String> dataIds) {
		Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();

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
		// Determines the input format from the referenced data and finds the chain of
		// converter algorithms to convert the data to the specified output format
		//
		// Arguments
		// dataId:
		// outFormat:

		// need to use getFormat()
		String algFormat="";
		Converter[] converters = null;
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		try {
			if (dataId != null && outFormat != null) {
				Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();
				if (dataMapping.containsKey(dataId)) {//dataId Present
					//algFormat = .getFormat();//check if the input format is correct
					if(dataConversionService != null) {
						converters = dataConversionService.findConverters(dataMapping.get(dataId), outFormat);
						for(Converter converter: converters) {
							//getAlgorithmFactory asks for CISHELLContext. Shall we create one in the CIBridge 
							converter.getAlgorithmFactory().createAlgorithm(dataMapping.get(dataId), null,cibridge.getBundleContext() );
						}
					}
					else {
						// throw error that dataConversionService is null
						throw new Exception("error that dataConversionService is null");
					}	
				}
				else {
					// throw error that dataId is not found.
					throw new Exception("error that dataId is not found");
				}
				
			} else {
				throw new Exception("Data ID or Output Format is null");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}

	@Override
	public List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat) {
		// Find the chain of converter algorithms to convert data from given input format
		// to given output format
		// 
		// Arguments
		// inFormat:
		// outFormat:
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		Converter[] converters = null;
		try {
			if(inFormat != null && outFormat != null) {
				if(dataConversionService != null) {
					converters = dataConversionService.findConverters(inFormat, outFormat);
					//FIXME: returned is converters
					//We need to return algorithm instances
				}
				else {
					throw new Exception("dataConversionService is null");
				}
			}
			else {
				throw new Exception("inFormat or outFormat is null");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public DataQueryResults getData(DataFilter filter) {
		//  Returns references for all the data objects that matches the given filter
		// 
		//  Arguments
		//  filter:
		
		//FIXME:
		//Take inputs from Varun about the filter object
		return null;
	}

	@Override
	public String downloadData(String dataId) {
		// Returns references for all the data objects that matches the given filter
		//
		// Arguments
		// filter:
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		try {
			if(dataId != null) {
				Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();
				if (dataMapping.containsKey(dataId)) {
					
				}
				else {
					throw new Exception("dataMapping does not have the given DataID");
				}
			}else {
				throw new Exception("dataID is null");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
