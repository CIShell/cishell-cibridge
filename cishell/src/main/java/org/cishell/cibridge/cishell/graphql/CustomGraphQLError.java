package org.cishell.cibridge.cishell.graphql;

import java.util.List;
import java.util.Map;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class CustomGraphQLError implements GraphQLError {
	private static final long serialVersionUID = 1L;
	private String message;
	private ErrorType errorType;
	private List<Object> path;
	private Map<String, Object> extensions;

	public CustomGraphQLError() {
		// TODO Auto-generated constructor stub
	}

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
		// TODO Auto-generated method stub
		return message;
	}

	@Override
	public List<SourceLocation> getLocations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	@Override
	public Map<String, Object> getExtensions() {
		// TODO Auto-generated method stub
		return extensions;
	}

}
