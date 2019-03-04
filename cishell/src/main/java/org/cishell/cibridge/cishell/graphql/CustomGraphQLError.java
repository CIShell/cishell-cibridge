package org.cishell.cibridge.cishell.graphql;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;
import java.util.Map;

public class CustomGraphQLError implements GraphQLError {

    private static final long serialVersionUID = 1L;
    private String message;
    private ErrorType errorType;
    private List<Object> path;
    private Map<String, Object> extensions;

    public CustomGraphQLError(GraphQLError e) {
        this.message = e.getMessage();
        this.errorType = e.getErrorType();
        this.path = e.getPath();
        this.extensions = e.getExtensions();
    }

    @Override
    public String toString() {
        return "CustomGraphQLError [message=" + message + ", errorTyoe=" + errorType + ", path=" + path
                + ", extensions=" + extensions + "]";
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public List<Object> getPath() {
        return path;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }

}
