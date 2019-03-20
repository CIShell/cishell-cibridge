package org.cishell.cibridge.cishell.graphql;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Arrays;

public class CIBridgeGraphQLServlet {

    private static final long serialVersionUID = 1L;

    private HttpService httpService;
    private BundleContext bundleContext;
    private HttpServlet simpleGraphQLHttpServlet;
    private String endpoint;

    public CIBridgeGraphQLServlet(BundleContext bundleContext, HttpServlet simpleGraphQLHttpServlet, HttpService httpService, String endpoint) {
        this.bundleContext = bundleContext;
        this.simpleGraphQLHttpServlet = simpleGraphQLHttpServlet;
        this.httpService = httpService;
        this.endpoint = endpoint;
    }

    public void start() {
        try {
            // Store the current CCL
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();

            // We have to set the CCL to Jetty's bundle class loader
            BundleWiring bundleWiring = findJettyBundle().adapt(BundleWiring.class);
            ClassLoader classLoader = bundleWiring.getClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);

            httpService.registerServlet(endpoint, simpleGraphQLHttpServlet, null, null);
            // Restore the CCL
            Thread.currentThread().setContextClassLoader(ccl);

        } catch (ServletException | NamespaceException e) {
            e.printStackTrace();
        }
    }

    private Bundle findJettyBundle() {
        return Arrays.stream(bundleContext.getBundles())
                .filter(b -> b.getSymbolicName().equals("org.apache.felix.http.jetty")).findAny().get();
    }

    public void unregister() {
        httpService.unregister(endpoint);
    }
}