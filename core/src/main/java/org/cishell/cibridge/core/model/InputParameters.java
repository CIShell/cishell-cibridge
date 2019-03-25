package org.cishell.cibridge.core.model;

import java.util.List;

public class InputParameters {
    private final String id;
    private String title;
    private String description;
    private List<ParameterDefinition> parameters;

    public InputParameters(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<ParameterDefinition> getParameters() {
        return parameters;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParameters(List<ParameterDefinition> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "InputParameters{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
