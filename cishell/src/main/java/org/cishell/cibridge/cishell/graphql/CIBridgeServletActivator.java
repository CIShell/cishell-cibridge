package org.cishell.cibridge.cishell.graphql;

import graphql.servlet.DefaultExecutionStrategyProvider;
import graphql.servlet.ExecutionStrategyProvider;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import graphql.servlet.GraphQLServletListener;
import graphql.servlet.GraphQLWebsocketServlet;
import graphql.servlet.SimpleGraphQLHttpServlet;
import graphql.servlet.internal.WsSessionSubscriptions;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.scheduler.SchedulerService;
import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.cishell.service.conversion.DataConversionService;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.osgi.framework.*;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.osgi.framework.Constants.OBJECTCLASS;

public class CIBridgeServletActivator implements BundleActivator {
	private BundleContext bundleContext;
	private ServiceTracker ciShellServicesTracker;
	private CIShellCIBridge ciBridge;
	private ServiceRegistration graphiqlServletRegistration;
	private ServiceRegistration graphQLServletRegistration;

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
		graphiqlServletRegistration.unregister();
		graphQLServletRegistration.unregister();
	}

	private void startCIBridge() {
		Hashtable<String, String> graphiqlServletProperties = new Hashtable<>();
		graphiqlServletProperties.put("osgi.http.whiteboard.servlet.pattern", "/graphiql");
		graphiqlServletProperties.put("alias", "/graphiql");
		graphiqlServletProperties.put("osgi.http.whiteboard.servlet.name", "graphiql");
		graphiqlServletRegistration = bundleContext.registerService(
				new String[] { HttpServlet.class.getName(), Servlet.class.getName() }, new GraphiqlServlet(),
				graphiqlServletProperties);

		Hashtable<String, String> graphQLServletProperties = new Hashtable<>();
		graphQLServletProperties.put("osgi.http.whiteboard.servlet.pattern", "/graphql");
		graphQLServletProperties.put("alias", "/graphql");
		graphQLServletProperties.put("osgi.http.whiteboard.servlet.name", "cibridge");

		Hashtable<String, Object> subscriptionServletProperties = new Hashtable<>();
		subscriptionServletProperties.put("osgi.http.whiteboard.servlet.pattern", "/subscriptions");
		subscriptionServletProperties.put("alias", "/subscriptions");
		subscriptionServletProperties.put("osgi.http.whiteboard.servlet.name", "cibridgeSubscriptions");
		subscriptionServletProperties.put("websocket.active", Boolean.TRUE);

		this.ciBridge = new CIShellCIBridge(bundleContext);
		CIBridgeGraphQLSchemaProvider ciBridgeGraphQLSchemaProvider = new CIBridgeGraphQLSchemaProvider(ciBridge);
		SimpleGraphQLHttpServlet graphQLServlet = SimpleGraphQLHttpServlet.newBuilder(ciBridgeGraphQLSchemaProvider)
				.build();

		graphQLServletRegistration = bundleContext.registerService(
				new String[] { HttpServlet.class.getName(), Servlet.class.getName() }, graphQLServlet,
				graphQLServletProperties);

//		GraphQLInvocationInputFactory factory = GraphQLInvocationInputFactory.newBuilder(ciBridgeGraphQLSchemaProvider)
//				.build();
//
//		DefaultExecutionStrategyProvider provider = new DefaultExecutionStrategyProvider();
//		GraphQLQueryInvoker queryInvoker = GraphQLQueryInvoker.newBuilder().withExecutionStrategyProvider(provider)
//				.build();
//
//		GraphQLObjectMapper graphQLObjectMapper = GraphQLObjectMapper.newBuilder()
//				.withGraphQLErrorHandler(ciBridgeGraphQLSchemaProvider).build();

//		GraphQLWebsocketServlet subscriptionServlet = new GraphQLWebsocketServlet(queryInvoker, factory,
//				graphQLObjectMapper);
		
		EchoServlet echoServlet = new EchoServlet(ciBridge);
		echoServlet.m_bundleContext = this.bundleContext;
		System.out.println("Starting subscriptions");
		echoServlet.m_httpService = (HttpService)this.getService(HttpService.class);
		echoServlet.start();
		
		CIBridgeWebSocketServlet subscriptionServlet = new CIBridgeWebSocketServlet();

		subscriptionServlet.configure(new WebSocketServletFactory() {

			@Override
			public boolean acceptWebSocket(HttpServletRequest request, HttpServletResponse response)
					throws IOException {
				// TODO Auto-generated method stub
				System.out.println('1');
				return true;
			}

			@Override
			public boolean acceptWebSocket(WebSocketCreator creator, HttpServletRequest request,
					HttpServletResponse response) throws IOException {
				// TODO Auto-generated method stub
				System.out.println('2');
				return true;
			}

			@Override
			public WebSocketCreator getCreator() {
				System.out.println('3');
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ExtensionFactory getExtensionFactory() {
				System.out.println('4');
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public WebSocketPolicy getPolicy() {
				System.out.println('5');
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isUpgradeRequest(HttpServletRequest request, HttpServletResponse response) {
				System.out.println('6');
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void register(Class<?> websocketPojo) {
				System.out.println('7');
				// TODO Auto-generated method stub

			}

			@Override
			public void setCreator(WebSocketCreator creator) {
				System.out.println('8');
				// TODO Auto-generated method stub

			}

			@Override
			public void start() throws Exception {
				System.out.println('9');
				// TODO Auto-generated method stub

			}

			@Override
			public void stop() throws Exception {
				System.out.println("10");
				// TODO Auto-generated method stub

			}

		});

//		BundleWiring bundleWiring = findHttpService().adapt(BundleWiring.class);
//		ServletContextHandler handler = (ServletContextHandler)ServletContextHandler.getContextHandler();

//		graphQLServletRegistration = bundleContext.registerService(
//				new String[] { HttpServlet.class.getName(), Servlet.class.getName() }, subscriptionServlet,
//				subscriptionServletProperties);

		graphQLServlet.addListener(new GraphQLServletListener() {
			@Override
			public GraphQLServletListener.RequestCallback onRequest(HttpServletRequest request,
					HttpServletResponse response) {

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getService(Class c) {
		ServiceReference ref = bundleContext.getServiceReference(c.getName());
		return ref != null ? bundleContext.getService(ref) : null;
	}

	private Bundle findJettyBundle() {
		return Arrays.stream(bundleContext.getBundles())
				.filter(b -> b.getSymbolicName().equals("org.apache.felix.http.jetty")).findAny().get();
	}

	private Bundle findHttpService() {
		return Arrays.stream(bundleContext.getBundles())
				.filter(b -> b.getSymbolicName().equals("org.osgi.service.http.HttpService")).findAny().get();
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