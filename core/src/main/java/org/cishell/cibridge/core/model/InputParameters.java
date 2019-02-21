package org.cishell.cibridge.core.model;

import java.util.List;

public class InputParameters {
    public final String id;
    public final String title;
    public final String description;
    public final List<ParameterDefinition> parameters;

    public InputParameters(String id, String title, String description, List<ParameterDefinition> parameters) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.parameters = parameters;
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

}
