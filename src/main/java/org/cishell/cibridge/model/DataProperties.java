package org.cishell.cibridge.model;

public class DataProperties {
	private String format;
	private String name;
	private String label;
	private String parent;
	private DataType type;
	private PropertyInput properties;
	private int limit;
	private int offset;
    
    public DataProperties(String format,String name,String label,String parent,DataType type,PropertyInput properties,int limit,int offset) {
		// TODO Auto-generated constructor stub
    	this.format=format;
    	this.name=name;
    	this.label=label;
    	this.parent=parent;
    	this.type=type;
    	this.properties=properties;
    	this.limit=limit;
    	this.offset=offset;
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

	public String getParent() {
		return parent;
	}

	public DataType getType() {
		return type;
	}

	public PropertyInput getProperties() {
		return properties;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public void setProperties(PropertyInput properties) {
		this.properties = properties;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
    
}
