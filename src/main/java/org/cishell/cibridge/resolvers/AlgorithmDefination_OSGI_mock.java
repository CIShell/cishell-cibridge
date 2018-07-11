package org.cishell.cibridge.resolvers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
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

	public AlgorithmDefination_OSGI_mock() {// ID not being initialized now, will be done later with correct
											// implementation
		// transitive objects beign created for algorithmDefinationQuerResult
		List<AlgorithmDefinition> algorithmDefinitionResults;
		PageInfo pageInfo1;
		InputParameters inputParameters;
		List<ParameterDefinition> parameterDefinationList;

		// transitive objects beign created for algorithmInstanceQueryResults
		AlgorithmInstance algorithmInstanceResults;
		PageInfo pageInfo2;
		List<String> inData = new ArrayList<String>();
		List<String> outData= new ArrayList<String>();
		Data inDataData;
		Data outDataData;
		List<Data> sendingInData = new ArrayList<Data>();
		List<Data> sendingOutData = new ArrayList<Data>();
		List<AlgorithmInstance> listAlgorithmInstanceResults;

		List<Property> inputList = new ArrayList<Property>();
		inputList.add(new Property("dummy key 1 in Property Class", "dummy value as well"));
		
		/* initialize algorithmDefinitionResults and pageInfo */
		parameterDefinationList = new ArrayList<ParameterDefinition>();

		parameterDefinationList.add(new ParameterDefinition("parameterDefination_1", "Dummy 1 in Parameter defination",
				"Don't know what type is in parameter defintion", AttributeType.STRING, inputList));
		inputList.add(new Property("dummy key 2 in Property Class", "dummy value as well"));
		parameterDefinationList.add(new ParameterDefinition("parameterDefination_2", "Dummy 2 in Parameter defination",
				"Don't know what type is in parameter defintion", AttributeType.STRING, inputList));
		inputParameters = new InputParameters("inpurParameter_1", "Title is dummy", "I hope this works",
				parameterDefinationList);
		algorithmDefinitionResults = new ArrayList<AlgorithmDefinition>();
		// algorithmDefinitionResults.add(new
		// AlgorithmDefinition("algorithmDefination_1", inputParameters, "inData1",
		// "outData1", "label:something", "desc:plis work", true,
		// AlgorithmType.STANDARD, true, "menuPath:strange",
		// ConversionType.LOSSLESS, "authors:Jack Sparrow", "implementers:Davy Jones",
		// "integrators:Barbosa",
		// "documentationUrl:http://atDeadMansChest", "some reference",
		// "referenceUrl:http://AskCalypso",
		// "writtenIn:jibbersih", inputList));
//		inputList.add(new Property("dummy key 3 in Property Class", "dummy value as well"));
		inData.add(new String("Data_1"));
//		inputList.add(new Property("dummy key 4 in Property Class", "dummy value as well"));
		outData.add(new String("Data_2"));
//		outData.add(new String("Data_2", "String format", "YOu know who", "Why Label?", "Parent ID not found", DataType.MODEL, true, inputList));
		algorithmDefinitionResults.add(new AlgorithmDefinition("algorithmDefination_1", inputParameters, inData,
				outData, "label:something", "desc:plis work", true, AlgorithmType.STANDARD, true, "menuPath:strange", ConversionType.LOSSLESS, "authors:Jack Sparrow",
				"implementers:Davy Jones", "integrators:Barbosa","documentationUrl:http://atDeadMansChest",  "some reference", "referenceUrl:http://AskCalypso", "writtenIn:jibbersih", inputList));
		pageInfo1 = new PageInfo(true, false);
		/* initialize algorithmDefinitionResults and pageInfo */
		algorithmDefinationQuerResult = new AlgorithmDefinitionQueryResults(algorithmDefinitionResults, pageInfo1);

		/* initialize algorithmInstanceResults and pageInfo */
		inputList.add(new Property("dummy key 3 in Property Class", "dummy value as well"));
		inDataData = new Data("Data_3", "String format", "YOu know who", "Why Label?", "Parent ID not found", DataType.MODEL, true, inputList);
		sendingInData.add(inDataData);
		inputList.add(new Property("dummy key 4 in Property Class", "dummy value as well"));
		outDataData = new Data("Data_4", "String format", "YOu know who", "Why Label?", "Parent ID not found", DataType.MODEL, true, inputList);
		sendingOutData.add(outDataData);
		algorithmInstanceResults = new AlgorithmInstance("algorithmInstance_1", sendingInData,inputList,algorithmDefinitionResults.get(0), AlgorithmState.SCHEDULED, new java.util.Date(System.currentTimeMillis()), 50, sendingOutData);
		pageInfo2 = new PageInfo(true, false);
		/* initialize algorithmInstanceResults and pageInfo */
		listAlgorithmInstanceResults = new ArrayList<AlgorithmInstance>();
		listAlgorithmInstanceResults.add(algorithmInstanceResults);
		algorithmInstanceQueryResults = new AlgorithmInstanceQueryResults(listAlgorithmInstanceResults, pageInfo2);

	}

	public AlgorithmDefinitionQueryResults getAlgorithmDefinationQuerResult() {
		return algorithmDefinationQuerResult;
	}

	public AlgorithmInstanceQueryResults getAlgorithmInstanceQueryResults() {
		return algorithmInstanceQueryResults;
	}

	// more functions to be defined

}
