package org.cishell.cibridge.core.model;

import java.util.List;

public class ParameterDefinition {
    private final String id;
    private String name;
    private String description;
    private AttributeType type;
    private int cardinality = 0;
    private List<String> defaultValues;
    private List<Property> options;

    public ParameterDefinition(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    public List<String> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(List<String> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public List<Property> getOptions() {
        return options;
    }

    public void setOptions(List<Property> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "ParameterDefinition{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", cardinality=" + cardinality +
                ", defaultValues=" + defaultValues +
                ", options=" + options +
                '}';
    }
}
