package org.cishell.cibridge;

import javax.servlet.annotation.WebServlet;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

import org.cishell.cibridge.mock.MockCIBridgeSchema;

@WebServlet(urlPatterns = "/graphql")
public class CIBridgeServlet extends SimpleGraphQLServlet {
	public CIBridgeServlet() {
		this(MockCIBridgeSchema.buildSchema());
	}
	public CIBridgeServlet(GraphQLSchema schema) {
		super(SimpleGraphQLServlet.builder(schema));
	}
}