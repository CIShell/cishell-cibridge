package org.cishell.cibridge.cishell.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.LocalCIShellContext;
import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.core.model.AlgorithmDefinition;
import org.cishell.cibridge.core.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.core.model.AlgorithmFilter;
import org.cishell.cibridge.core.model.AlgorithmInstance;
import org.cishell.cibridge.core.model.AlgorithmState;
import org.cishell.cibridge.core.model.AlgorithmType;
import org.cishell.cibridge.core.model.ConversionType;
import org.cishell.cibridge.core.model.Data;
import org.cishell.cibridge.core.model.DataFilter;
import org.cishell.cibridge.core.model.DataProperties;
import org.cishell.cibridge.core.model.DataQueryResults;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.service.conversion.Converter;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import sun.print.resources.serviceui;

public class CIShellCIBridgeDataFacade implements CIBridge.DataFacade {
	private CIShellCIBridge cibridge;
	private final HashMap<String, Long> algorithmInstanceCount = new HashMap<>();

	public void setCIBridge(CIShellCIBridge cibridge) {
		this.cibridge = cibridge;
	}

	@Override
	public String validateData(String algorithmDefinitionId, List<String> dataIds) {
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

	private AlgorithmDefinition setAlgorithmDefinition(ServiceReference ref) {

        AlgorithmDefinition algorithmDefinition = new AlgorithmDefinition(
                ref.getProperty(Constants.SERVICE_PID) != null ? ref.getProperty(Constants.SERVICE_PID).toString() : null,
                null,//InputParameters parameters
                ref.getProperty(AlgorithmProperty.IN_DATA) != null ? Arrays.asList(ref.getProperty(AlgorithmProperty.IN_DATA).toString()) : null,//List<String> inData
                ref.getProperty(AlgorithmProperty.OUT_DATA) != null ? Arrays.asList(ref.getProperty(AlgorithmProperty.OUT_DATA).toString()) : null,//List<String> outData
                ref.getProperty(AlgorithmProperty.LABEL) != null ? ref.getProperty(AlgorithmProperty.LABEL).toString() : null,//String label
                ref.getProperty(AlgorithmProperty.DESCRIPTION) != null ? ref.getProperty(AlgorithmProperty.DESCRIPTION).toString() : null,//String description
                null,//Boolean parentOutputData
                ref.getProperty(AlgorithmProperty.ALGORITHM_TYPE) != null ? AlgorithmType.valueOf(ref.getProperty(AlgorithmProperty.ALGORITHM_TYPE).toString()) : null,//AlgorithmType type
                ref.getProperty(AlgorithmProperty.REMOTEABLE) != null ? Boolean.valueOf(ref.getProperty(AlgorithmProperty.REMOTEABLE).toString()) : null,//String menuPath,//Boolean remoteable
                ref.getProperty(AlgorithmProperty.MENU_PATH) != null ? ref.getProperty(AlgorithmProperty.MENU_PATH).toString() : null,//String menuPath
                ref.getProperty(AlgorithmProperty.CONVERSION) != null ? ConversionType.valueOf(ref.getProperty(AlgorithmProperty.CONVERSION).toString()) : null,//ConversionType conversion
                ref.getProperty(AlgorithmProperty.AUTHORS) != null ? ref.getProperty(AlgorithmProperty.AUTHORS).toString() : null,//String authors
                ref.getProperty(AlgorithmProperty.IMPLEMENTERS) != null ? ref.getProperty(AlgorithmProperty.IMPLEMENTERS).toString() : null,//String implementers
                ref.getProperty(AlgorithmProperty.INTEGRATORS) != null ? ref.getProperty(AlgorithmProperty.INTEGRATORS).toString() : null,//String integrators
                ref.getProperty(AlgorithmProperty.DOCUMENTATION_URL) != null ? ref.getProperty(AlgorithmProperty.DOCUMENTATION_URL).toString() : null,//String documentationUrl
                ref.getProperty(AlgorithmProperty.REFERENCE) != null ? ref.getProperty(AlgorithmProperty.REFERENCE).toString() : null,//String reference
                ref.getProperty(AlgorithmProperty.REFERENCE_URL) != null ? ref.getProperty(AlgorithmProperty.REFERENCE_URL).toString() : null,//String referenceUrl
                ref.getProperty(AlgorithmProperty.WRITTEN_IN) != null ? ref.getProperty(AlgorithmProperty.WRITTEN_IN).toString() : null,//String writtenIn
                null//List<Property> otherProperties
        );

        System.out.println("AlgorithmDefinition created: " + algorithmDefinition);
        return algorithmDefinition;
    }
	
	private String generateAndGetInstanceId(String algorithmDefinitionId) {
        Long newCount;

        if (algorithmInstanceCount.get(algorithmDefinitionId) != null) {
            newCount = algorithmInstanceCount.get(algorithmDefinitionId) + 1;
        } else {
            newCount = 0L;
        }

        algorithmInstanceCount.put(algorithmDefinitionId, newCount);
        return algorithmDefinitionId + newCount;
    }
	
	@Override //complete
	public List<AlgorithmInstance> findConverters(String dataId, String outFormat) {
		List<AlgorithmInstance> algorithmInstanceList = new ArrayList<AlgorithmInstance>();
		String algFormat="";
		Converter[] converters = null;
		List<AlgorithmDefinition> algorithmDefinitions = new ArrayList<>();
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		List<org.cishell.framework.data.Data> tempDataList;
		List<Data> inDataList = new ArrayList<>();
		try {
			if (dataId != null && outFormat != null) {
				Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();
				inDataList.add((Data) dataMapping.get(dataId).getData());
				if (dataMapping.containsKey(dataId)) {
					if(dataConversionService != null) {
						converters = dataConversionService.findConverters(dataMapping.get(dataId), outFormat);
						for(Converter converter: converters) {
							ServiceReference[] converterServiceReferences = converter.getConverterChain();
							if(converterServiceReferences != null && converterServiceReferences.length > 0) {
								for(ServiceReference ref : converterServiceReferences) {
									algorithmDefinitions.add(setAlgorithmDefinition(ref));
								}
								for(AlgorithmDefinition algorithmDefinition: algorithmDefinitions) {
									String algorithmInstanceId = generateAndGetInstanceId(algorithmDefinition.getId());
									AlgorithmInstance algorithmInstance = new AlgorithmInstance(algorithmInstanceId, inDataList, algorithmDefinition.getOtherProperties(), algorithmDefinition, AlgorithmState.IDLE, null, 1, null);
									algorithmInstanceList.add(algorithmInstance);
									inDataList.clear();
									inDataList.add((Data) algorithmInstance.getOutData());
								}
								
							}
							else {
								throw new Exception("converterServiceReference List is empty");
							}
							break;
						}
					}
					else {
						throw new Exception("error that dataConversionService is null");
					}
				}
				else {
					throw new Exception("error that dataId is not found");
				}

			} else {
				throw new Exception("Data ID or Output Format is null");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return algorithmInstanceList;
	}

	@Override
	public List<AlgorithmInstance> findConvertersByFormat(String inFormat, String outFormat) {
		List<Data> inDataList = new ArrayList<>();
		List<AlgorithmInstance> algorithmInstanceList = new ArrayList<AlgorithmInstance>();
		List<AlgorithmDefinition> algorithmDefinitions = new ArrayList<>();
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		Converter[] converters = null;
		try {
			if(inFormat != null && outFormat != null) {
				if(dataConversionService != null) {
					converters = dataConversionService.findConverters(inFormat, outFormat);
					for(Converter converter: converters) {
						ServiceReference[] converterServiceReferences = converter.getConverterChain();
						if(converterServiceReferences != null && converterServiceReferences.length > 0) {
							for(ServiceReference ref : converterServiceReferences) {
								algorithmDefinitions.add(setAlgorithmDefinition(ref));
							}
							for(AlgorithmDefinition algorithmDefinition: algorithmDefinitions) {
								String algorithmInstanceId = generateAndGetInstanceId(algorithmDefinition.getId());
								AlgorithmInstance algorithmInstance = new AlgorithmInstance(algorithmInstanceId, inDataList, algorithmDefinition.getOtherProperties(), algorithmDefinition, AlgorithmState.IDLE, null, 1, null);
								algorithmInstanceList.add(algorithmInstance);
								inDataList.clear();
								inDataList.add((Data) algorithmInstance.getOutData());
							}
							
						}
						else {
							throw new Exception("converterServiceReference List is empty");
						}
						break;
					}
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
		return algorithmInstanceList;
	}

	@Override 
	public DataQueryResults getData(DataFilter filter) {
		DataQueryResults dataQueryResults = null;
		List<Data> results;
		try {
			if(filter != null) {
				if(!(filter.getDataIds().isEmpty())){
					Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();
					results = new ArrayList<Data>();
					for(String dataId: filter.getDataIds()) {
						Data retrievedData = (Data)dataMapping.get(dataId).getData();
						results.add(retrievedData);
					}
					dataQueryResults = new DataQueryResults(results, null);
				}
			}
			else {
				throw new Exception();
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return dataQueryResults;
	}

	@Override
	public String downloadData(String dataId) {
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		try {
			if(dataId != null) {
				Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();
				if (dataMapping.containsKey(dataId)) {
					if(dataConversionService != null) {
						org.cishell.framework.data.Data tempData = dataMapping.get(dataId);
						if(tempData.getFormat().startsWith("file:")) {
							if(tempData.getData() instanceof File) {
								File f1 =  (File) tempData.getData();
								return f1.getAbsolutePath();
							}else {
								throw new Exception("Data not an instance of a file");
							}
						}
						else {
							throw new Exception("format of data not a file");
						}
					}
					else {
						throw new Exception("dataConversionService is null");
					}
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
		DataManagerService dataManagerService = cibridge.getDataManagerService();
		BasicData tempBasicData = null;
		try {
			if(file != null && properties != null) {
				File fr = new File(file.trim());
				tempBasicData = new BasicData(fr, properties.getFormat());
				dataManagerService.addData((org.cishell.framework.data.Data) tempBasicData.getData());
			}
			else {
				throw new Exception("file name null or properties null");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return (Data) tempBasicData.getData();
	}

	@Override
	public Boolean removeData(String dataId) {
		DataManagerService dataManagerService = cibridge.getDataManagerService();
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		try {
			if(dataId != null) {
				Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();
				if (dataMapping.containsKey(dataId)) {
					if(dataConversionService != null) {
						dataManagerService.removeData(dataMapping.get(dataId));
						if(!(dataMapping.containsKey(dataId))) {
							return true;
						}
						else {
							return false;
						}
					}
					else {
						throw new Exception("dataConversionService is null");
					}
				}
				else {
					throw new Exception("dataMapping does not have the given DataID");
				}
			}
			else {
				throw new Exception("DataID is null");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean updateData(String dataId, DataProperties properties) {
		DataManagerService dataManagerService = cibridge.getDataManagerService();
		DataConversionService dataConversionService = (DataConversionService) this.cibridge.getDataConversionService();
		try {
			if(dataId != null && properties.getLabel() != null) {
				Map<String, org.cishell.framework.data.Data> dataMapping = getDataMap();
				if (dataMapping.containsKey(dataId)) {
					if(dataConversionService != null) {
						dataManagerService.setLabel(dataMapping.get(dataId), properties.getLabel());
						return true;
					}
					else {
						throw new Exception("dataConversionService is null");
					}
				}
				else {
					throw new Exception("dataMapping does not have the given DataID");
				}
			}
			else {
				throw new Exception("Either Data ID or Data Properties are null strings");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	//FIXME: below are subscriptions
	@Override
	public Data dataAdded() {

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
