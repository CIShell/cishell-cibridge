package org.cishell.cibridge.cishell.graphql;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.service.http.HttpService;

import org.cishell.cibridge.cishell.graphql.CIBridgeServlet;
import org.cishell.cibridge.cishell.graphql.GraphiqlServlet;
import org.cishell.cibridge.cishell.CIShellCIBridge;

public class CIBridgeServletActivator implements BundleActivator {
    private ServiceTracker httpTracker;
  
    public void start(BundleContext context) throws Exception {
      httpTracker = new ServiceTracker(context, HttpService.class.getName(), null) {
        public void removedService(ServiceReference reference, Object service) {
          // HTTP service is no longer available, unregister our servlet...
          try {
             ((HttpService) service).unregister("/graphql");
             ((HttpService) service).unregister("/");
          } catch (IllegalArgumentException exception) {
             // Ignore; servlet registration probably failed earlier on...
          }
        }
  
        public Object addingService(ServiceReference reference) {
          // HTTP service is available, register our servlet...
          HttpService httpService = (HttpService) this.context.getService(reference);
          try {
            CIBridgeServlet servlet = new CIBridgeServlet();
            servlet.setBundleContext(context);

            httpService.registerServlet("/graphql", servlet, null, null);
            httpService.registerServlet("/", new GraphiqlServlet(), null, null);
          } catch (Exception exception) {
            exception.printStackTrace();
          }
          return httpService;
        }
      };
      // start tracking all HTTP services...
      httpTracker.open();
    }
  
    public void stop(BundleContext context) throws Exception {
      // stop tracking all HTTP services...
      httpTracker.close();
    }
  }