package org.cishell.cibridge.resolvers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.cishell.cibridge.model.AlgorithmDefinition;
import org.cishell.cibridge.model.AlgorithmDefinitionQueryResults;
import org.cishell.cibridge.model.AlgorithmInstance;
import org.cishell.cibridge.model.AlgorithmInstanceQueryResults;
import org.cishell.cibridge.model.AlgorithmState;
import org.cishell.cibridge.model.AlgorithmType;
import org.cishell.cibridge.model.AttributeType;
import org.cishell.cibridge.model.ConversionType;
import org.cishell.cibridge.model.Data;
import org.cishell.cibridge.model.DataType;
import org.cishell.cibridge.model.InputParameters;
import org.cishell.cibridge.model.PageInfo;
import org.cishell.cibridge.model.ParameterDefinition;
import org.cishell.cibridge.model.Property;

public class AlgorithmDefination_OSGI_mock {
	private final AlgorithmDefinitionQueryResults algorithmDefinationQuerResult;
	private final AlgorithmInstanceQueryResults algorithmInstanceQueryResults;
	
	public AlgorithmDefination_OSGI_mock() {//ID not being initialized now, will be done later with correct implementation
		//transitive objects beign created for algorithmDefinationQuerResult
		List<AlgorithmDefinition> algorithmDefinitionResults;
		PageInfo pageInfo1;
		InputParameters inputParameters;
		List<ParameterDefinition> parameterDefinationList;
		
		//transitive objects beign created for algorithmInstanceQueryResults
		AlgorithmInstance algorithmInstanceResults;
		PageInfo pageInfo2;
		Data inData;
		Data outData;
		
		
		/*initialize algorithmDefinitionResults and pageInfo */
		parameterDefinationList = new ArrayList<ParameterDefinition>();
		parameterDefinationList.add(new ParameterDefinition("parameterDefination_1", "Dummy 1 in Parameter defination", "Don't know what type is in parameter defintion",AttributeType.STRING,new Property("dummy key 1 in Property Class","dummy value as well")));
		parameterDefinationList.add(new ParameterDefinition("parameterDefination_2", "Dummy 2 in Parameter defination", "Don't know what type is in parameter defintion",AttributeType.STRING,new Property("dummy key 2 in Property Class","dummy value as well")));
		inputParameters = new InputParameters("inpurParameter_1","Title is dummy", "I hope this works",parameterDefinationList );
		algorithmDefinitionResults = new ArrayList<AlgorithmDefinition>();
		algorithmDefinitionResults.add(new AlgorithmDefinition("algorithmDefination_1",inputParameters, "inData1", "outData1", "label:something","desc:plis work", true  , AlgorithmType.STANDARD, true, "menuPath:strange", ConversionType.LOSSLESS, "authors:Jack Sparrow", "implementers:Davy Jones", "integrators:Barbosa", "documentationUrl:http://atDeadMansChest", "referenceUrl:http://AskCalypso", "writtenIn:jibbersih", new Property("dummy key 3 in Property Class","dummy value as well")));
		pageInfo1 = new PageInfo(true,false);
		/*initialize algorithmDefinitionResults and pageInfo */
		algorithmDefinationQuerResult =  new AlgorithmDefinitionQueryResults(algorithmDefinitionResults, pageInfo1);
		
		/*initialize algorithmInstanceResults and pageInfo */
		inData = new Data("data_1","format:string", "name:inData", "label:Don't know", "parentDataID:to be populated with parentDataID", DataType.MODEL, true, new Property("dummy key 4 in Property Class","dummy value as well"));
		outData = new Data("data_2","format:string", "name:outData", "label:Don't know", "parentDataID:to be populated with parentDataID", DataType.MODEL, true, new Property("dummy key 5 in Property Class","dummy value as well"));
		algorithmInstanceResults = new AlgorithmInstance("algorithmInstance_1",inData, new Property("dummy key 4 in Property Class","dummy value as in parameters"), algorithmDefinitionResults.get(0), AlgorithmState.SCHEDULED, LocalTime.NOON, 50, outData);
		pageInfo2 = new PageInfo(true,false);
		/*initialize algorithmInstanceResults and pageInfo */
		algorithmInstanceQueryResults = new AlgorithmInstanceQueryResults(algorithmInstanceResults, pageInfo2);
		
	}

	public AlgorithmDefinitionQueryResults getAlgorithmDefinationQuerResult() {
		return algorithmDefinationQuerResult;
	}

	public AlgorithmInstanceQueryResults getAlgorithmInstanceQueryResults() {
		return algorithmInstanceQueryResults;
	}
	
	//more functions to be defined
	
}
