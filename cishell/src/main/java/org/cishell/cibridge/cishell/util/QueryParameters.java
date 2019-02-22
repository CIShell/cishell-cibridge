package org.cishell.cibridge.cishell.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Graphql clients can send GET or POST HTTP requests. The spec does not make an
 * explicit distinction. So you may need to handle both. The following was
 * tested using a graphiql client tool found here :
 * https://github.com/skevy/graphiql-app
 *
 * You should consider bundling graphiql in your application
 *
 * https://github.com/graphql/graphiql
 *
 * This outlines more information on how to handle parameters over http
 *
 * http://graphql.org/learn/serving-over-http/
 */
public class QueryParameters {

	private String id;
	private String type;
	private String query;
	private String operationName;
	private Map<String, Object> variables = Collections.emptyMap();

	public String getType() {
		return type;
	}

	public String getID() {
		return id;
	}

	public String getQuery() {
		return query;
	}

	public String getOperationName() {
		return operationName;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public static QueryParameters from(String queryMessage) {
		QueryParameters parameters = new QueryParameters();
		Map<String, Object> json = JsonKit.toMap(queryMessage);
		parameters.operationName = (String) json.get("operationName");
		parameters.variables = getVariables(json.get("variables"));
		parameters.id = (String) json.get("id");
		parameters.type = (String) json.get("type");
		if (json.containsKey("payload")) {
			parameters.query = (String) ((Map<String, Object>) json.get("payload")).get("query");
		}
		return parameters;
	}

	private static Map<String, Object> getVariables(Object variables) {
		if (variables instanceof Map) {
			Map<?, ?> inputVars = (Map<?, ?>) variables;
			Map<String, Object> vars = new HashMap<>();
			inputVars.forEach((k, v) -> vars.put(String.valueOf(k), v));
			return vars;
		}
		return JsonKit.toMap(String.valueOf(variables));
	}

}