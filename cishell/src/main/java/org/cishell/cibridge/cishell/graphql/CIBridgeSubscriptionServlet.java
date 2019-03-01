package org.cishell.cibridge.cishell.graphql;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import graphql.GraphQL;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;

public class CIBridgeSubscriptionServlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;

	private HttpService httpService;
	private CIBridgeGraphQLSchemaProvider ciBridgeGraphQLSchemaProvider;
	private BundleContext bundleContext;
	public static GraphQL graphql;

	public CIBridgeSubscriptionServlet(CIBridgeGraphQLSchemaProvider ciBridgeGraphQLSchemaProvider,
			BundleContext bundleContext, HttpService httpService) {
		this.ciBridgeGraphQLSchemaProvider = ciBridgeGraphQLSchemaProvider;
		this.bundleContext = bundleContext;
		this.httpService = httpService;
		createGraphQLInstance();
	}

	public void start() {
		try {
			// Store the current CCL
			ClassLoader ccl = Thread.currentThread().getContextClassLoader();
			// We have to set the CCL to Jetty's bundle class loader
			BundleWiring bundleWiring = findJettyBundle().adapt(BundleWiring.class);
			ClassLoader classLoader = bundleWiring.getClassLoader();
			Thread.currentThread().setContextClassLoader(classLoader);
			httpService.registerServlet("/subscriptions", this, null, null);
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

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Sec-WebSocket-Protocol", "graphql-ws");
		super.service(request, response);
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(Integer.MAX_VALUE);
		factory.register(CIBridgeWebSocket.class);
	}

	private void createGraphQLInstance() {
		Instrumentation instrumentation = new ChainedInstrumentation(
				Collections.singletonList(new TracingInstrumentation()));
		graphql = GraphQL.newGraphQL(ciBridgeGraphQLSchemaProvider.getSchema())
				.subscriptionExecutionStrategy(new SubscriptionExecutionStrategy()).instrumentation(instrumentation)
				.build();
	}

	public HttpService getHttpService() {
		return httpService;
	}

	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}
