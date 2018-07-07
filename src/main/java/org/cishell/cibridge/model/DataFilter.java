package org.cishell.cibridge.model;

import java.util.ArrayList;
import java.util.List;

public class DataFilter {
	 private final List<Integer> dataIDs;
	 private final String formats;
	 private final Boolean isModified;
	 private final DataType types;
	 private final PropertyInput properties;

	public DataFilter(List<Integer> dataIDs,String formats,Boolean isModified,DataType types,PropertyInput properties) {
		// TODO Auto-generated constructor stub
    	this.dataIDs=dataIDs;
    	this.formats=formats;
    	this.isModified=isModified;
    	this.types=types;
    	this.properties=properties;
	}
	public List<Integer> getDataIDs() {
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
    
    
}
