package org.cishell.cibridge.cishell.graphql;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.Hashtable;
import javax.servlet.http.HttpServlet;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

import graphql.servlet.SimpleGraphQLHttpServlet;
import graphql.servlet.GraphQLServletListener;

import org.cishell.cibridge.graphql.schema.CIBridgeSchema;
import org.cishell.cibridge.cishell.graphql.CIBridgeServlet;
import org.cishell.cibridge.cishell.graphql.GraphiqlServlet;
import org.cishell.cibridge.cishell.graphql.CIBridgeGraphQLSchemaProvider;
import org.cishell.cibridge.cishell.CIShellCIBridge;


public class CIBridgeServletActivator implements BundleActivator {
    public static BundleContext bundleContext;
    private ServiceRegistration registration1;
    private ServiceRegistration registration2;

    public void start(BundleContext context) throws Exception {
      CIBridgeSchema.schemaString = CIBridgeServletActivator.entryToString("/cibridge-schema.graphqls");

      Hashtable props = new Hashtable();
      props.put("osgi.http.whiteboard.servlet.pattern", "/graphiql");
      props.put("alias", "/graphiql");
      props.put("osgi.http.whiteboard.servlet.name", "graphiql");
      GraphiqlServlet graphiql = new GraphiqlServlet();
      registration1 = context.registerService(new String[]{HttpServlet.class.getName(),Servlet.class.getName()}, graphiql, props);

      props = new Hashtable();
      props.put("osgi.http.whiteboard.servlet.pattern", "/graphql");
      props.put("alias", "/graphql");
      props.put("osgi.http.whiteboard.servlet.name", "cibridge");
      CIShellCIBridge cibridge = new CIShellCIBridge(context);
      CIBridgeGraphQLSchemaProvider cibridgeSchemaProvider = new CIBridgeGraphQLSchemaProvider(cibridge);
      SimpleGraphQLHttpServlet servlet = SimpleGraphQLHttpServlet.newBuilder(cibridgeSchemaProvider).build();
      registration2 = context.registerService(new String[]{HttpServlet.class.getName(),Servlet.class.getName()}, servlet, props);

      servlet.addListener(new GraphQLServletListener() {
        @Override
        public GraphQLServletListener.RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {
    
            return new GraphQLServletListener.RequestCallback() {
                @Override
                public void onSuccess(HttpServletRequest request, HttpServletResponse response) { }
    
                @Override
                public void onError(HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
                    throwable.printStackTrace();
                }
    
                @Override
                public void onFinally(HttpServletRequest request, HttpServletResponse response) { }
            };
        }
        });
    }
  
    public void stop(BundleContext context) throws Exception {
      registration1.unregister();
      registration2.unregister();
    }


    private static String entryToString(String entryPath) {
        try {
            InputStream inputStream = CIBridgeServletActivator.class.getResourceAsStream(entryPath);
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, "UTF-8");
            for (;;) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        } catch(Exception e) {
            return "";
        }
    }
  }