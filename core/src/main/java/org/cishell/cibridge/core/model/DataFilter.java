package org.cishell.cibridge.core.model;

import java.util.List;

public class DataFilter {
	private List<String> dataIds;
	private List<String> formats;
	private Boolean isModified;
	private List<DataType> types;
	private List<PropertyInput> properties;

	public DataFilter(){
		//allow empty instantiation
	}

	public DataFilter(List<String> dataIds, List<String> formats, Boolean isModified, List<DataType> types,
			List<PropertyInput> properties) {
		// TODO Auto-generated constructor stub
		this.dataIds = dataIds;
		this.formats = formats;
		this.isModified = isModified;
		this.types = types;
		this.properties = properties;
	}

	public List<String> getDataIds() {
		return dataIds;
	}

	public void setDataIds(List<String> dataIds) {
		this.dataIds = dataIds;
	}

	public List<String> getFormats() {
		return formats;
	}

	public void setFormats(List<String> formats) {
		this.formats = formats;
	}

	public Boolean getIsModified() {
		return isModified;
	}

	public void setIsModified(Boolean isModified) {
		this.isModified = isModified;
	}

	public List<DataType> getTypes() {
		return types;
	}

	public void setTypes(List<DataType> types) {
		this.types = types;
	}

	public List<PropertyInput> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyInput> properties) {
		this.properties = properties;
	}

}
