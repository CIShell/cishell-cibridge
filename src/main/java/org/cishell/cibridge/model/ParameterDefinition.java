package org.cishell.cibridge.model;

public class ParameterDefinition{
	private final String ID;
	private final String name;
	private final String description;
	private final AttributeType type;
	private final Property options;
    
    public ParameterDefinition(String ID,String name,String description,AttributeType type,Property options) {
		// TODO Auto-generated constructor stub
    	this.ID=ID;
    	this.name=name;
    	this.description=description;
    	this.type=type;
    	this.options=options;
	}
    public ParameterDefinition(String name,String description,AttributeType type,Property options) {
		// TODO Auto-generated constructor stub
    	this.ID=null;
    	this.name=name;
    	this.description=description;
    	this.type=type;
    	this.options=options;
	}

	public String getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public AttributeType getType() {
		return type;
	}

	public Property getOptions() {
		return options;
	}
    
}
