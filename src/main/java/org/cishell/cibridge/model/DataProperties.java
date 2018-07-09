package org.cishell.cibridge.model;

public class DataProperties {
	private final String format;
	private final String name;
	private final String label;
	private final String parent;
	private final DataType type;
	private final PropertyInput properties;
	private final int limit;
	private final int offset;
    
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
    
}
