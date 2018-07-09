package org.cishell.cibridge.model;

import java.util.ArrayList;
import java.util.List;

public class DataFilter {
	 private List<String> dataIDs;
	 private String formats;
	 private Boolean isModified;
	 private DataType types;
	 private PropertyInput properties;

	public DataFilter(List<String> dataIDs,String formats,Boolean isModified,DataType types,PropertyInput properties) {
		// TODO Auto-generated constructor stub
    	this.dataIDs=dataIDs;
    	this.formats=formats;
    	this.isModified=isModified;
    	this.types=types;
    	this.properties=properties;
	}
	public List<String> getDataIDs() {
		return dataIDs;
	}
	public String getFormats() {
		return formats;
	}
	public Boolean getIsModified() {
		return isModified;
	}
	public DataType getTypes() {
		return types;
	}
	public PropertyInput getProperties() {
		return properties;
	}
	public void setDataIDs(List<String> dataIDs) {
		this.dataIDs = dataIDs;
	}
	public void setFormats(String formats) {
		this.formats = formats;
	}
	public void setIsModified(Boolean isModified) {
		this.isModified = isModified;
	}
	public void setTypes(DataType types) {
		this.types = types;
	}
	public void setProperties(PropertyInput properties) {
		this.properties = properties;
	}
	
	
    
    
}
