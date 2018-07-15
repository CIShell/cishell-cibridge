package org.cishell.cibridge.mock;

import javax.servlet.annotation.WebServlet;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import org.cishell.cibridge.mock.schema.MockGraphQLSchema;

@WebServlet(urlPatterns = "/graphql")
public class CIBridgeServlet extends SimpleGraphQLServlet {
	public CIBridgeServlet() {
		this(MockGraphQLSchema.buildSchema());
	}
	public CIBridgeServlet(GraphQLSchema schema) {
		super(SimpleGraphQLServlet.builder(schema));
	}
}