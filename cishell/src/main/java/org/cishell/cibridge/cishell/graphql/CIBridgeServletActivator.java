package org.cishell.cibridge.cishell.graphql;

import java.util.Hashtable;
import javax.servlet.http.HttpServlet;
import javax.servlet.Servlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

import org.cishell.cibridge.cishell.graphql.CIBridgeServlet;
import org.cishell.cibridge.cishell.graphql.GraphiqlServlet;
import org.cishell.cibridge.cishell.CIShellCIBridge;

public class CIBridgeServletActivator implements BundleActivator {
    private ServiceRegistration registration1;
    private ServiceRegistration registration2;

    public void start(BundleContext context) throws Exception {
      Hashtable props = new Hashtable();
      props.put("osgi.http.whiteboard.servlet.pattern", "/graphiql");
      props.put("alias", "/graphiql");
      props.put("osgi.http.whiteboard.servlet.name", "graphiql");
      GraphiqlServlet graphiql = new GraphiqlServlet();
      registration1 = context.registerService(new String[]{HttpServlet.class.getName(),Servlet.class.getName()}, graphiql, props);

      props = new Hashtable();
      props.put("osgi.http.whiteboard.servlet.pattern", "/cibridge/graphql");
      props.put("osgi.http.whiteboard.servlet.name", "cibridge");
      CIBridgeServlet servlet = new CIBridgeServlet();
      servlet.setBundleContext(context);
      registration2 = context.registerService(new String[]{HttpServlet.class.getName(),Servlet.class.getName()}, servlet, props);
    }
  
    public void stop(BundleContext context) throws Exception {
      registration1.unregister();
      registration2.unregister();
    }
  }