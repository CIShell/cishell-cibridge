package org.cishell.cibridge.model;

import java.util.List;

public class ParameterDefinition {
	private final String id;
	private final String name;
	private final String description;
	private final AttributeType type;
	protected final List<Property> options;

	public ParameterDefinition(String id, String name, String description, AttributeType type, List<Property> options) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.options = options;
	}

	public ParameterDefinition(String name, String description, AttributeType type, List<Property> options) {
		// TODO Auto-generated constructor stub
		this.id = null;
		this.name = name;
		this.description = description;
		this.type = type;
		this.options = options;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public AttributeType getType() {
		return type;
	}

	public List<Property> getOptions() {
		return options;
	}

}
