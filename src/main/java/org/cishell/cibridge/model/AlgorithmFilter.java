package org.cishell.cibridge.model;

import java.util.List;

public class AlgorithmFilter {
	private final List<Integer> algortihmDefinitionIds;
	private final List<Integer> algortihmInstanceIds;
	private final AlgorithmState states;
	private final List<Integer> inputDataIds;
	private final String inputFormats;
	private final String outputFormats;
	private final PropertyInput properties;
	private final int limit;
	private final int offset;

	public AlgorithmFilter(List<Integer> algortihmDefinitionIds, List<Integer> algortihmInstanceIds,
			AlgorithmState states, List<Integer> inputDataIds, String inputFormats, String outputFormats,
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

	public List<Integer> getAlgortihmDefinitionIds() {
		return algortihmDefinitionIds;
	}

	public List<Integer> getAlgortihmInstanceIds() {
		return algortihmInstanceIds;
	}

	public AlgorithmState getStates() {
		return states;
	}

	public List<Integer> getInputDataIds() {
		return inputDataIds;
	}

	public String getInputFormats() {
		return inputFormats;
	}

	public String getOutputFormats() {
		return outputFormats;
	}

	public PropertyInput getProperties() {
		return properties;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}
	
}
