package org.cishell.cibridge.model;

import java.util.ArrayList;
import java.util.List;

public class DataFilter {
	 private List<String> dataIDs;
	 private List<String> formats;
	 private Boolean isModified;
	 private List<DataType> types;
	 private List<PropertyInput> properties;

	public DataFilter(List<String> dataIDs,List<String> formats,Boolean isModified,List<DataType> types,List<PropertyInput> properties) {
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
	public List<String> getFormats() {
		return formats;
	}
	public Boolean getIsModified() {
		return isModified;
	}
	public List<DataType> getTypes() {
		return types;
	}
	public List<PropertyInput> getProperties() {
		return properties;
	}
	public void setDataIDs(List<String> dataIDs) {
		this.dataIDs = dataIDs;
	}
	public void setFormats(List<String> formats) {
		this.formats = formats;
	}
	public void setIsModified(Boolean isModified) {
		this.isModified = isModified;
	}
	public void setTypes(List<DataType> types) {
		this.types = types;
	}
	public void setProperties(List<PropertyInput> properties) {
		this.properties = properties;
	}
	
	
    
    
}
