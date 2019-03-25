package org.cishell.cibridge.core.model;

import java.time.ZonedDateTime;
import java.util.List;

public class AlgorithmInstance {
    private final String id;
    private List<Data> inData;
    private List<Property> parameters;
    private final AlgorithmDefinition algorithmDefinition;
    private AlgorithmState state = AlgorithmState.IDLE;
    private ZonedDateTime scheduledRunTime;
    private int progress = 0;
    private List<Data> outData;

    public AlgorithmInstance(String id, AlgorithmDefinition algorithmDefinition) {
        this.id = id;
        this.algorithmDefinition = algorithmDefinition;
    }

    public String getId() {
        return id;
    }

    public List<Data> getInData() {
        return inData;
    }

    public void setInData(List<Data> inData) {
        this.inData = inData;
    }

    public List<Property> getParameters() {
        return parameters;
    }

    public void setParameters(List<Property> parameters) {
        this.parameters = parameters;
    }

    public AlgorithmDefinition getAlgorithmDefinition() {
        return algorithmDefinition;
    }

    public AlgorithmState getState() {
        return state;
    }

    public void setState(AlgorithmState state) {
        this.state = state;
    }

    public ZonedDateTime getScheduledRunTime() {
        return scheduledRunTime;
    }

    public void setScheduledRunTime(ZonedDateTime scheduledRunTime) {
        this.scheduledRunTime = scheduledRunTime;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public List<Data> getOutData() {
        return outData;
    }

    public void setOutData(List<Data> outData) {
        this.outData = outData;
    }

    @Override
    public String toString() {
        return "AlgorithmInstance{" +
                "id='" + id + '\'' +
                ", inData=" + inData +
                ", parameters=" + parameters +
                ", algorithmDefinition=" + algorithmDefinition +
                ", state=" + state +
                ", scheduledRunTime=" + scheduledRunTime +
                ", progress=" + progress +
                ", outData=" + outData +
                '}';
    }
}
