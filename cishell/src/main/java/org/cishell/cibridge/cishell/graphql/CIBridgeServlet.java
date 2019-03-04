package org.cishell.cibridge.cishell.graphql;

import graphql.servlet.SimpleGraphQLHttpServlet;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.cibridge.core.CIBridge;
import org.cishell.container.CIShellContainer;
import org.osgi.framework.BundleContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/graphql")
public class CIBridgeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CIBridgeGraphQLSchemaProvider cibridgeSchemaProvider;
    private BundleContext bundleContext;
    private SimpleGraphQLHttpServlet simpleGraphQLHttpServlet;


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        simpleGraphQLHttpServlet.service(req, resp);

    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet Initialized...");
        if (this.bundleContext == null) {
            System.out.println("Starting Container...");
            CIShellContainer ciContainer = CIShellContainer.getBuilder().build();
            this.setBundleContext(ciContainer.getBundleContext());
        }
        this.setCIBridge(new CIShellCIBridge(bundleContext));
    }

    public CIBridgeServlet() {
        this(null);// need to give bundle context as an input today
        //this(new MockCIBridge(null)); //todo: Just for development, should be new NullCIBridge().
        System.out.println("Here is first constructor of CIBridge Servlet");
    }

    public CIBridgeServlet(CIBridge cibridge) {
        this.cibridgeSchemaProvider = new CIBridgeGraphQLSchemaProvider(null);
        simpleGraphQLHttpServlet = SimpleGraphQLHttpServlet.newBuilder(this.cibridgeSchemaProvider).build();

    }

    public void setBundleContext(BundleContext context) {
        this.bundleContext = context;
    }

    public BundleContext getBundleContext() {
        return this.bundleContext;
    }

    public void setCIBridge(CIBridge cibridge) {
        this.cibridgeSchemaProvider.setCIBridge(cibridge);
    }

    public CIBridge getCIBridge() {
        return this.cibridgeSchemaProvider.getCIBridge();
    }
}