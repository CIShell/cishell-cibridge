package org.cishell.cibridge.model;

public class Property {
	public final String key;
	public final Object value;

	public Property(String key,Object value) {
		// TODO Auto-generated constructor stub
		this.key=key;
		this.value=value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}
	
}
