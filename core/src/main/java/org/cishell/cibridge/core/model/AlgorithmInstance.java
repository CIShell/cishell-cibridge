package org.cishell.cibridge.core.model;

import java.time.ZonedDateTime;
import java.util.List;

public class AlgorithmInstance {
    private final String id;
    private final List<Data> inData;
    private final List<Property> parameters;
    private final AlgorithmDefinition algorithmDefinition;
    private AlgorithmState state;
    private ZonedDateTime scheduledRunTime;
    private final int progress;
    private final List<Data> outData;

    public AlgorithmInstance(String id, List<Data> inData, List<Property> parameters,
                             AlgorithmDefinition algorithmDefinition, AlgorithmState state, ZonedDateTime scheduledRunTime, int progress,
                             List<Data> outData) {
        // TODO Auto-generated constructor stub
        this.id = id;
        this.inData = inData;
        this.parameters = parameters;
        this.algorithmDefinition = algorithmDefinition;
        this.state = state;
        this.scheduledRunTime = scheduledRunTime;
        this.progress = progress;
        this.outData = outData;
    }

    public AlgorithmInstance(List<Data> inData, List<Property> parameters, AlgorithmDefinition algorithmDefinition,
                             AlgorithmState state, ZonedDateTime scheduledRunTime, int progress, List<Data> outData) {
        // TODO Auto-generated constructor stub
        this.id = null;
        this.inData = inData;
        this.parameters = parameters;
        this.algorithmDefinition = algorithmDefinition;
        this.state = state;
        this.scheduledRunTime = scheduledRunTime;
        this.progress = progress;
        this.outData = outData;
    }

    public String getId() {
        return id;
    }

    public List<Data> getInData() {
        return inData;
    }

    public List<Property> getParameters() {
        return parameters;
    }

    public AlgorithmDefinition getAlgorithmDefinition() {
        return algorithmDefinition;
    }

    public AlgorithmState getState() {
        return state;
    }

    public ZonedDateTime getScheduledRunTime() {
        return scheduledRunTime;
    }

    public int getProgress() {
        return progress;
    }

    public List<Data> getOutData() {
        return outData;
    }

    public void setScheduledRunTime(ZonedDateTime scheduledRunTime) {
        this.scheduledRunTime = scheduledRunTime;
    }

    public void setState(AlgorithmState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AlgorithmInstance [id=" + id + ", inData=" + inData + ", parameters=" + parameters
                + ", algorithmDefinition=" + algorithmDefinition + ", state=" + state + ", scheduledRunTime="
                + scheduledRunTime + ", progress=" + progress + ", outData=" + outData + "]";
    }


}
