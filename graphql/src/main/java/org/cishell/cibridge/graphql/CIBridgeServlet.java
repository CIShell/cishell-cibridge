package org.cishell.cibridge.graphql;

import javax.servlet.annotation.WebServlet;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.mock.MockCIBridge;

import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class CIBridgeServlet extends SimpleGraphQLServlet {
	private static final long serialVersionUID = 1L;
	private final CIBridgeGraphQLSchemaProvider cibridgeSchemaProvider;
	
	public CIBridgeServlet() {
		this(new MockCIBridge(null)); // FIXME: Just for development, should be new NullCIBridge().
	}
	public CIBridgeServlet(CIBridge cibridge) {
		super(new Builder(new CIBridgeGraphQLSchemaProvider(cibridge)));
		this.cibridgeSchemaProvider = (CIBridgeGraphQLSchemaProvider) this.getSchemaProvider();
	}
	
	public void setCIBridge(CIBridge cibridge) {
		this.cibridgeSchemaProvider.setCIBridge(cibridge);
	}
	public CIBridge getCIBridge() {
		return this.cibridgeSchemaProvider.getCIBridge();
	}
}