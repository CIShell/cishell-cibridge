package org.cishell.cibridge.model;

import java.util.List;

public class AlgorithmFilter {
	private List<String> algortihmDefinitionIds;
	private List<String> algortihmInstanceIds;
	private AlgorithmState states;
	private List<String> inputDataIds;
	private String inputFormats;
	private String outputFormats;
	private PropertyInput properties;
	private int limit;
	private int offset;

	public AlgorithmFilter(List<String> algortihmDefinitionIds, List<String> algortihmInstanceIds,
			AlgorithmState states, List<String> inputDataIds, String inputFormats, String outputFormats,
			PropertyInput properties, int limit, int offset) {
		// TODO Auto-generated constructor stub
		this.algortihmDefinitionIds = algortihmDefinitionIds;
		this.algortihmInstanceIds = algortihmInstanceIds;
		this.states = states;
		this.inputDataIds = inputDataIds;
		this.inputFormats = inputFormats;
		this.outputFormats = outputFormats;
		this.properties = properties;
		this.limit = limit;
		this.offset = offset;
	}

	public List<String> getAlgortihmDefinitionIds() {
		return algortihmDefinitionIds;
	}

	public void setAlgortihmDefinitionIds(List<String> algortihmDefinitionIds) {
		this.algortihmDefinitionIds = algortihmDefinitionIds;
	}

	public List<String> getAlgortihmInstanceIds() {
		return algortihmInstanceIds;
	}

	public void setAlgortihmInstanceIds(List<String> algortihmInstanceIds) {
		this.algortihmInstanceIds = algortihmInstanceIds;
	}

	public AlgorithmState getStates() {
		return states;
	}

	public void setStates(AlgorithmState states) {
		this.states = states;
	}

	public List<String> getInputDataIds() {
		return inputDataIds;
	}

	public void setInputDataIds(List<String> inputDataIds) {
		this.inputDataIds = inputDataIds;
	}

	public String getInputFormats() {
		return inputFormats;
	}

	public void setInputFormats(String inputFormats) {
		this.inputFormats = inputFormats;
	}

	public String getOutputFormats() {
		return outputFormats;
	}

	public void setOutputFormats(String outputFormats) {
		this.outputFormats = outputFormats;
	}

	public PropertyInput getProperties() {
		return properties;
	}

	public void setProperties(PropertyInput properties) {
		this.properties = properties;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	
	
	
}
