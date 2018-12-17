package org.cishell.cibridge.cishell.graphql;

import graphql.servlet.GraphQLServletListener;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;


public class CIBridgeServletActivator implements BundleActivator {
    private ServiceRegistration registration1;
    private ServiceRegistration registration2;

    public void start(BundleContext context) {
        Hashtable props = new Hashtable();
        props.put("osgi.http.whiteboard.servlet.pattern", "/graphiql");
        props.put("alias", "/graphiql");
        props.put("osgi.http.whiteboard.servlet.name", "graphiql");
        GraphiqlServlet graphiql = new GraphiqlServlet();
        registration1 = context.registerService(new String[]{HttpServlet.class.getName(), Servlet.class.getName()}, graphiql, props);

        props = new Hashtable();
        props.put("osgi.http.whiteboard.servlet.pattern", "/graphql");
        props.put("alias", "/graphql");
        props.put("osgi.http.whiteboard.servlet.name", "cibridge");
        CIShellCIBridge cibridge = new CIShellCIBridge(context);
        CIBridgeGraphQLSchemaProvider cibridgeSchemaProvider = new CIBridgeGraphQLSchemaProvider(cibridge);
        SimpleGraphQLHttpServlet servlet = SimpleGraphQLHttpServlet.newBuilder(cibridgeSchemaProvider).build();
        registration2 = context.registerService(new String[]{HttpServlet.class.getName(), Servlet.class.getName()}, servlet, props);

        servlet.addListener(new GraphQLServletListener() {
            @Override
            public GraphQLServletListener.RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {

                return new GraphQLServletListener.RequestCallback() {
                    @Override
                    public void onSuccess(HttpServletRequest request, HttpServletResponse response) {
                    }

                    @Override
                    public void onError(HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
                        System.out.println("error");
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onFinally(HttpServletRequest request, HttpServletResponse response) {
                    }
                };
            }
        });
    }

    public void stop(BundleContext context) throws Exception {
        registration1.unregister();
        registration2.unregister();
    }
}