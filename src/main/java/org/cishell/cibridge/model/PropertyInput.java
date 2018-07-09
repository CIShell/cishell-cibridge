package org.cishell.cibridge.model;

public class PropertyInput {
	private String key;
	private Object value;

	public PropertyInput(String key, Object value) {
		// TODO Auto-generated constructor stub
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	

}
