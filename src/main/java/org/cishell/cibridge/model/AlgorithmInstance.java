package org.cishell.cibridge.model;
import java.time.LocalTime;

public class AlgorithmInstance {
    private final String ID;
    private final Data inData;
    private final Property parameters;
    private final AlgorithmDefinition algorithmDefinition;
    private final AlgorithmState state;
    private final LocalTime scheduledRunTime;
    private final int progress;
    private final Data outData;
    
    public AlgorithmInstance(String ID,Data inData,Property parameters,AlgorithmDefinition algorithmDefinition,AlgorithmState state,LocalTime scheduledRunTime,int progress,Data outData) {
		// TODO Auto-generated constructor stub
    	this.ID=ID;
    	this.inData=inData;
    	this.parameters=parameters;
    	this.algorithmDefinition=algorithmDefinition;
    	this.state=state;
    	this.scheduledRunTime=scheduledRunTime;
    	this.progress=progress;
    	this.outData=outData;
	}
    public AlgorithmInstance(Data inData,Property parameters,AlgorithmDefinition algorithmDefinition,AlgorithmState state,LocalTime scheduledRunTime,int progress,Data outData) {
		// TODO Auto-generated constructor stub
    	this.ID=null;
    	this.inData=inData;
    	this.parameters=parameters;
    	this.algorithmDefinition=algorithmDefinition;
    	this.state=state;
    	this.scheduledRunTime=scheduledRunTime;
    	this.progress=progress;
    	this.outData=outData;
	}

	public String getID() {
		return ID;
	}

	public Data getInData() {
		return inData;
	}

	public Property getParameters() {
		return parameters;
	}

	public AlgorithmDefinition getAlgorithmDefinition() {
		return algorithmDefinition;
	}

	public AlgorithmState getState() {
		return state;
	}

	public LocalTime getScheduledRunTime() {
		return scheduledRunTime;
	}

	public int getProgress() {
		return progress;
	}

	public Data getOutData() {
		return outData;
	}
    
    
}
