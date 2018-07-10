package org.cishell.cibridge.model;

import java.util.List;

public class AlgorithmFilter {
	private List<String> algortihmDefinitionIds;
	private List<String> algortihmInstanceIds;
	private List<AlgorithmState> states;
	private List<String> inputDataIds;
	private List<String> inputFormats;
	private List<String> outputFormats;
	private List<PropertyInput> properties;
	private int limit;
	private int offset;

	public AlgorithmFilter(List<String> algortihmDefinitionIds, List<String> algortihmInstanceIds,
			List<AlgorithmState> states, List<String> inputDataIds, List<String> inputFormats, List<String> outputFormats,
			List<PropertyInput> properties, int limit, int offset) {
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

	public List<AlgorithmState> getStates() {
		return states;
	}

	public void setStates(List<AlgorithmState> states) {
		this.states = states;
	}

	public List<String> getInputDataIds() {
		return inputDataIds;
	}

	public void setInputDataIds(List<String> inputDataIds) {
		this.inputDataIds = inputDataIds;
	}

	public List<String> getInputFormats() {
		return inputFormats;
	}

	public void setInputFormats(List<String> inputFormats) {
		this.inputFormats = inputFormats;
	}

	public List<String> getOutputFormats() {
		return outputFormats;
	}

	public void setOutputFormats(List<String> outputFormats) {
		this.outputFormats = outputFormats;
	}

	public List<PropertyInput> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyInput> properties) {
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
