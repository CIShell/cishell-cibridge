package org.cishell.cibridge.mock.graphql;

import graphql.servlet.SimpleGraphQLServlet;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.mock.MockCIBridge;

import javax.servlet.annotation.WebServlet;

//import org.cishell.cibridge.cishell.CIShellCIBridge;

@WebServlet(urlPatterns = "/graphql")
public class MockCIBridgeServlet extends SimpleGraphQLServlet {
    private static final long serialVersionUID = 1L;
    private final MockGraphQLSchemaProvider cibridgeSchemaProvider;

    public MockCIBridgeServlet() {
//		this(new CIShellCIBridge(null));// need to give bundle context as an input today
        this(new MockCIBridge()); // FIXME: Just for development, should be new NullCIBridge().
        System.out.println("Here is first constructor of CIBridge Servlet");
    }

    public MockCIBridgeServlet(CIBridge cibridge) {
        super(new Builder(new MockGraphQLSchemaProvider(cibridge)));
        System.out.println("Here is second constructor of CIBridge Servlet");
        this.cibridgeSchemaProvider = (MockGraphQLSchemaProvider) this.getSchemaProvider();
    }

    public void setCIBridge(CIBridge cibridge) {
        this.cibridgeSchemaProvider.setCIBridge(cibridge);
    }

    public CIBridge getCIBridge() {
        return this.cibridgeSchemaProvider.getCIBridge();
    }
}