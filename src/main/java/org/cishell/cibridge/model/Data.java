package org.cishell.cibridge.model;
import java.util.*;
public class Data {
    private final String id;
    private final String format;
    private final String name;
    private final String label;
    private final String parentDataID;
    private final DataType type;
    private final Boolean isModified;
    private final List<Property> properties;
    
    public Data(String id,String format,String name,String label,String parentDataID,DataType type,Boolean isModified,List<Property> properties) {
		// TODO Auto-generated constructor stub
    	this.id=id;
    	this.format=format;
    	this.name=name;
    	this.label=label;
    	this.parentDataID=parentDataID;
    	this.type=type;
    	this.isModified=isModified;
    	this.properties=properties;
	}

	public String getID() {
		return id;
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
	public List<Property> getProperties() {
		return properties;
	}
    
    
    
}
