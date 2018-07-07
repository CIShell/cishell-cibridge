package org.cishell.cibridge.model;

public class Data {
    private final String ID;
    private final String format;
    private final String name;
    private final String label;
    private final String parentDataID;
    private final DataType type;
    private final Boolean isModified;
    private final Property properties;
    
    public Data(String ID,String format,String name,String label,String parentDataID,DataType type,Boolean isModified,Property properties) {
		// TODO Auto-generated constructor stub
    	this.ID=ID;
    	this.format=format;
    	this.name=name;
    	this.label=label;
    	this.parentDataID=parentDataID;
    	this.type=type;
    	this.isModified=isModified;
    	this.properties=properties;
	}
    public Data(String format,String name,String label,String parentDataID,DataType type,Boolean isModified,Property properties) {
		// TODO Auto-generated constructor stub
    	this.ID=null;
    	this.format=format;
    	this.name=name;
    	this.label=label;
    	this.parentDataID=parentDataID;
    	this.type=type;
    	this.isModified=isModified;
    	this.properties=properties;
	}
	public String getID() {
		return ID;
	}
	public String getFormat() {
		return format;
	}
	public String getName() {
		return name;
	}
	public String getLabel() {
		return label;
	}
	public String getParentDataID() {
		return parentDataID;
	}
	public DataType getType() {
		return type;
	}
	public Boolean getIsModified() {
		return isModified;
	}
	public Property getProperties() {
		return properties;
	}
    
    
    
}
