package org.cishell.cibridge.cishell;

import javax.servlet.annotation.WebServlet;

import org.cishell.cibridge.cishell.schema.CIShellGraphQLSchema;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class CIBridgeServlet extends SimpleGraphQLServlet {
	public CIBridgeServlet() {
		this(CIShellGraphQLSchema.buildSchema());
	}
	public CIBridgeServlet(GraphQLSchema schema) {
		super(SimpleGraphQLServlet.builder(schema));
	}
}