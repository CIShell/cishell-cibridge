package org.cishell.cibridge.cishell.graphql;

import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.scheduler.SchedulerService;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.framework.*;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.osgi.framework.Constants.OBJECTCLASS;

public class CIBridgeServletActivator implements BundleActivator {

    private BundleContext bundleContext;
    private ServiceTracker<Object, Object> ciShellServicesTracker;
    private CIShellCIBridge ciBridge;
    private CIBridgeSubscriptionServlet subscriptionServlet;
    private CIBridgeGraphQLServlet ciBridgeGraphiQLServlet;
    private CIBridgeGraphQLServlet ciBridgeGraphQLServlet;

    private static final Set<String> CISHELL_SERVICES = new HashSet<>(Arrays.asList(DataManagerService.class.getName(),
            SchedulerService.class.getName(), DataConversionService.class.getName(), LogService.class.getName(),
            MetaTypeService.class.getName(), HttpService.class.getName()));

    @Override
    public void start(BundleContext bundleContext) {
        this.bundleContext = bundleContext;

        // create filter criteria for the services the tracker will track
        StringBuilder filterString = new StringBuilder("(|");
        for (String svcName : CISHELL_SERVICES) {
            filterString.append("(" + OBJECTCLASS + "=").append(svcName).append(")");
        }
        filterString.append(")");

        Filter filter;
        try {
            // create filter with the filter string
            filter = bundleContext.createFilter(filterString.toString());
            // create the service tracker for the cishell service and mark it open
            ciShellServicesTracker = new ServiceTracker<>(bundleContext, filter,
                    new CIShellServicesTrackerCustomizer<>());
            ciShellServicesTracker.open();
        } catch (InvalidSyntaxException ignored) {
        }
    }

    @Override
    public void stop(BundleContext bundleContext) {
        // close the tracker opened in the start method
        if (ciShellServicesTracker != null) {
            ciShellServicesTracker.close();
        }
        stopCIBridge();
    }

    private void stopCIBridge() {
        // unregister all the services registered by this bundle
        ciBridgeGraphQLServlet.unregister();
        ciBridgeGraphiQLServlet.unregister();
        subscriptionServlet.unregister();
    }

    private void startCIBridge() {

        HttpService httpservice = (HttpService) this.getService(HttpService.class);

        ciBridgeGraphiQLServlet = new CIBridgeGraphQLServlet(bundleContext, new GraphiqlServlet(), httpservice, "/graphiql");
        ciBridgeGraphiQLServlet.start();

        this.ciBridge = new CIShellCIBridge(bundleContext);
        CIBridgeGraphQLSchemaProvider ciBridgeGraphQLSchemaProvider = new CIBridgeGraphQLSchemaProvider(ciBridge);

        // Maps errors
        GraphQLObjectMapper graphQLObjectMapper = GraphQLObjectMapper.newBuilder().withGraphQLErrorHandler(ciBridgeGraphQLSchemaProvider).build();
        SimpleGraphQLHttpServlet graphQLServlet = SimpleGraphQLHttpServlet.newBuilder(ciBridgeGraphQLSchemaProvider).withObjectMapper(graphQLObjectMapper)
                .build();

        ciBridgeGraphQLServlet = new CIBridgeGraphQLServlet(bundleContext, graphQLServlet, httpservice, "/graphql");
        ciBridgeGraphQLServlet.start();

        subscriptionServlet = new CIBridgeSubscriptionServlet(ciBridgeGraphQLSchemaProvider,
                bundleContext, httpservice, "/subscriptions");
        subscriptionServlet.start();

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object getService(Class<HttpService> c) {
        ServiceReference ref = bundleContext.getServiceReference(c.getName());
        return ref != null ? bundleContext.getService(ref) : null;
    }

    private class CIShellServicesTrackerCustomizer<S, T> implements ServiceTrackerCustomizer<S, T> {

        private Set<String> unavailableServices = new HashSet<>(CISHELL_SERVICES);

        @Override
        public synchronized T addingService(ServiceReference<S> serviceReference) {
            List<String> addedCIShellServices = Arrays.stream((String[]) serviceReference.getProperty(OBJECTCLASS))
                    .filter(CISHELL_SERVICES::contains).collect(Collectors.toList());

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
                    .filter(CISHELL_SERVICES::contains).collect(Collectors.toList());

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