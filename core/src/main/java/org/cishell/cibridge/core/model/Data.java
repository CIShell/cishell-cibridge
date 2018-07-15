package org.cishell.cibridge.core.model;
import java.util.*;
public class Data {
    private final String id;
    private final String format;
    private final String name;
    private final String label;
    private final String parentDataId;
    private final DataType type;
    private final Boolean isModified;
    private final List<Property> properties;
    
    public Data(String id,String format,String name,String label,String parentDataId,DataType type,Boolean isModified,List<Property> properties) {
		// TODO Auto-generated constructor stub
    	this.id=id;
    	this.format=format;
    	this.name=name;
    	this.label=label;
    	this.parentDataId=parentDataId;
    	this.type=type;
    	this.isModified=isModified;
    	this.properties=properties;
	}
    public Data(String format,String name,String label,String parentDataId,DataType type,Boolean isModified,List<Property> properties) {
		// TODO Auto-generated constructor stub
    	this.id=null;
    	this.format=format;
    	this.name=name;
    	this.label=label;
    	this.parentDataId=parentDataId;
    	this.type=type;
    	this.isModified=isModified;
    	this.properties=properties;
	}

	public String getId() {
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

	public String getParentDataId() {
		return parentDataId;
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
