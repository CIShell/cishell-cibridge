package org.cishell.cibridge.core.model;

import java.util.List;

public class AlgorithmFilter {
    private List<String> algorithmDefinitionIds;
    private List<String> algorithmInstanceIds;
    private List<AlgorithmState> states;
    private List<String> inputDataIds;
    private List<String> inputFormats;
    private List<String> outputFormats;
    private List<PropertyInput> properties;
    private int limit = 0;
    private int offset = 0;

    public AlgorithmFilter() {
        //allow empty instantiation
    }

    public AlgorithmFilter(List<String> algorithmDefinitionIds, List<String> algorithmInstanceIds,
                           List<AlgorithmState> states, List<String> inputDataIds, List<String> inputFormats, List<String> outputFormats,
                           List<PropertyInput> properties, int limit, int offset) {
        this.algorithmDefinitionIds = algorithmDefinitionIds;
        this.algorithmInstanceIds = algorithmInstanceIds;
        this.states = states;
        this.inputDataIds = inputDataIds;
        this.inputFormats = inputFormats;
        this.outputFormats = outputFormats;
        this.properties = properties;
        this.limit = limit;
        this.offset = offset;
    }

    public List<String> getAlgorithmDefinitionIds() {
        return algorithmDefinitionIds;
    }

    public void setAlgorithmDefinitionIds(List<String> algorithmDefinitionIds) {
        this.algorithmDefinitionIds = algorithmDefinitionIds;
    }

    public List<String> getAlgorithmInstanceIds() {
        return algorithmInstanceIds;
    }

    public void setAlgorithmInstanceIds(List<String> algorithmInstanceIds) {
        this.algorithmInstanceIds = algorithmInstanceIds;
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

    @Override
    public String toString() {
        return "AlgorithmFilter [algorithmDefinitionIds=" + algorithmDefinitionIds + ", algorithmInstanceIds="
                + algorithmInstanceIds + ", states=" + states + ", inputDataIds=" + inputDataIds + ", inputFormats="
                + inputFormats + ", outputFormats=" + outputFormats + ", properties=" + properties + ", limit=" + limit
                + ", offset=" + offset + "]";
    }

}
