package org.cishell.cibridge.graphql;

import javax.servlet.annotation.WebServlet;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.mock.MockCIBridge;

import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class CIBridgeServlet extends SimpleGraphQLServlet {
	private static final long serialVersionUID = 1L;
	private final CIBridgeGraphQLSchemaProvider cibridgeSchemaProvider;
	
	public CIBridgeServlet() {
		this(new CIShellCIBridge(null));// need to give bundle context as an input today
		//this(new MockCIBridge(null)); // FIXME: Just for development, should be new NullCIBridge().
		System.out.println("Here is first constructor of CIBridge Servlet");
	}
	public CIBridgeServlet(CIBridge cibridge) {
		super(new Builder(new CIBridgeGraphQLSchemaProvider(cibridge)));
		System.out.println("Here is second constructor of CIBridge Servlet");
		this.cibridgeSchemaProvider = (CIBridgeGraphQLSchemaProvider) this.getSchemaProvider();
	}
	
	public void setCIBridge(CIBridge cibridge) {
		this.cibridgeSchemaProvider.setCIBridge(cibridge);
	}
	public CIBridge getCIBridge() {
		return this.cibridgeSchemaProvider.getCIBridge();
	}
}