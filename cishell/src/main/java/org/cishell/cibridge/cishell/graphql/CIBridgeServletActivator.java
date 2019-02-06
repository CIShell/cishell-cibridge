package org.cishell.cibridge.cishell.graphql;

import graphql.servlet.GraphQLServletListener;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.scheduler.SchedulerService;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.framework.*;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static org.osgi.framework.Constants.OBJECTCLASS;

public class CIBridgeServletActivator implements BundleActivator {
    private BundleContext bundleContext;
    private ServiceTracker ciShellServicesTracker;
    private CIShellCIBridge ciBridge;
    private ServiceRegistration graphiqlServletRegistration;
    private ServiceRegistration graphQLServletRegistration;

    private static final Set<String> CISHELL_SERVICES = new HashSet<>(Arrays.asList(
            DataManagerService.class.getName(),
            SchedulerService.class.getName(),
            DataConversionService.class.getName(),
            LogService.class.getName(),
            MetaTypeService.class.getName()
    )
    );

    @Override
    public void start(BundleContext bundleContext) {
        this.bundleContext = bundleContext;

        //create filter criteria for the services the tracker will track
        StringBuilder filterString = new StringBuilder("(|");
        for (String svcName : CISHELL_SERVICES) {
            filterString.append("(" + OBJECTCLASS + "=").append(svcName).append(")");
        }
        filterString.append(")");

        Filter filter;
        try {
            //create filter with the filter string
            filter = bundleContext.createFilter(filterString.toString());

            //create the service tracker for the cishell service and mark it open
            ciShellServicesTracker = new ServiceTracker<>(bundleContext, filter, new CIShellServicesTrackerCustomizer<>());
            ciShellServicesTracker.open();
        } catch (InvalidSyntaxException ignored) {
        }
    }

    @Override
    public void stop(BundleContext bundleContext) {
        //close the tracker opened in the start method
        if (ciShellServicesTracker != null) {
            ciShellServicesTracker.close();
        }
        stopCIBridge();
    }

    private void stopCIBridge() {
        //unregister all the services registered by this bundle
        graphiqlServletRegistration.unregister();
        graphQLServletRegistration.unregister();
    }

    private void startCIBridge() {
        Hashtable<String, String> graphiqlServletProperties = new Hashtable<>();
        graphiqlServletProperties.put("osgi.http.whiteboard.servlet.pattern", "/graphiql");
        graphiqlServletProperties.put("alias", "/graphiql");
        graphiqlServletProperties.put("osgi.http.whiteboard.servlet.name", "graphiql");
        graphiqlServletRegistration = bundleContext.registerService(new String[]{HttpServlet.class.getName(), Servlet.class.getName()}, new GraphiqlServlet(), graphiqlServletProperties);

        Hashtable<String, String> graphQLServletProperties = new Hashtable<>();
        graphQLServletProperties.put("osgi.http.whiteboard.servlet.pattern", "/graphql");
        graphQLServletProperties.put("alias", "/graphql");
        graphQLServletProperties.put("osgi.http.whiteboard.servlet.name", "cibridge");

        this.ciBridge = new CIShellCIBridge(bundleContext);
        CIBridgeGraphQLSchemaProvider ciBridgeGraphQLSchemaProvider = new CIBridgeGraphQLSchemaProvider(ciBridge);
        SimpleGraphQLHttpServlet graphQLServlet = SimpleGraphQLHttpServlet.newBuilder(ciBridgeGraphQLSchemaProvider).build();
        graphQLServletRegistration = bundleContext.registerService(new String[]{HttpServlet.class.getName(), Servlet.class.getName()}, graphQLServlet, graphQLServletProperties);

        graphQLServlet.addListener(new GraphQLServletListener() {
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


    private class CIShellServicesTrackerCustomizer<S, T> implements ServiceTrackerCustomizer<S, T> {

        private Set<String> unavailableServices = new HashSet<>(CISHELL_SERVICES);

        @Override
        public synchronized T addingService(ServiceReference<S> serviceReference) {
            List<String> addedCIShellServices = Arrays.stream((String[]) serviceReference.getProperty(OBJECTCLASS))
                    .filter(CISHELL_SERVICES::contains)
                    .collect(Collectors.toList());

            unavailableServices.removeIf(addedCIShellServices::contains);

            if (ciBridge == null && unavailableServices.size() == 0) {
                startCIBridge();
            }

            return null;
        }

        @Override
        public void modifiedService(ServiceReference<S> serviceReference, T t) {
        }

        @Override
        public void removedService(ServiceReference<S> serviceReference, T t) {

            System.out.println("Removed : " + serviceReference);
            List<String> removedCIShellServices = Arrays.stream((String[]) serviceReference.getProperty(OBJECTCLASS))
                    .filter(CISHELL_SERVICES::contains)
                    .collect(Collectors.toList());


            System.out.println("Removed CIShell services: " + removedCIShellServices);

            for (String service : removedCIShellServices) {
                if (bundleContext.getServiceReference(service) == null) {
                    unavailableServices.add(service);
                }
            }

            if (unavailableServices.size() > 0) {
                stopCIBridge();
            }
        }
    }
}