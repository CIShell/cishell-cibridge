package org.cishell.cibridge.cishell.graphql;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import org.cishell.cibridge.cishell.util.JsonKit;
import org.cishell.cibridge.cishell.util.QueryParameters;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;

public class CIBridgeWebSocket extends WebSocketAdapter {

	private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();

	@Override
	@OnWebSocketConnect
	public void onWebSocketConnect(Session session) {
		System.out.println("session open");
		super.onWebSocketConnect(session);
//		session.setIdleTimeout(10000);
		System.out.println(session.isOpen());
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		System.out.println("Session closed");
		super.onWebSocketClose(statusCode, reason);
		Subscription subscription = subscriptionRef.get();
		if (subscription != null) {
			subscription.cancel();
		}
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		System.out.println("Web Socket Error");
		cause.printStackTrace();
	}

	@Override
	public void onWebSocketText(String graphqlQuery) {

		System.out.println(graphqlQuery);
		try {
			this.getRemote().sendString("{\"type\":\"connection_ack\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}

		QueryParameters parameters = QueryParameters.from(graphqlQuery);

		if (parameters.getType().equals("stop")) {
			System.out.println("Printing stopping");
			getSession().close();
		} else if (parameters.getQuery() != null) {
			
			String id = (String) parameters.getID();
			System.out.println("*********************************************");
			System.out.println(parameters.getQuery());

			ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(parameters.getQuery())
					.variables(parameters.getVariables()).operationName(parameters.getOperationName()).build();

			Instrumentation instrumentation = new ChainedInstrumentation(
					Collections.singletonList(new TracingInstrumentation()));

			CIBridgeGraphQLSchemaProvider ciBridgeGraphQLSchemaProvider = new CIBridgeGraphQLSchemaProvider(
					CIBridgeSubscriptionServlet.getCiBridge());

			GraphQL graphQL = GraphQL.newGraphQL(ciBridgeGraphQLSchemaProvider.getSchema())
					.subscriptionExecutionStrategy(new SubscriptionExecutionStrategy()).instrumentation(instrumentation)
					.build();

			ExecutionResult executionResult = graphQL.execute(executionInput);

			Publisher<ExecutionResult> stockPriceStream = executionResult.getData();

			stockPriceStream.subscribe(new Subscriber<ExecutionResult>() {

				@Override
				public void onSubscribe(Subscription s) {
					subscriptionRef.set(s);
					request(1);
				}

				@Override
				public void onNext(ExecutionResult er) {
					try {
						Object stockPriceUpdate = er.getData();
						String jsonData = JsonKit.toJsonString(stockPriceUpdate);
						System.out.println("*****************************");
						System.out.println(jsonData);
						getRemote().sendString("{\"id\": \"" + id + "\", \"type\":\"data\", \"payload\": {\"data\":"
								+ jsonData + "}}");
					} catch (IOException e) {
						e.printStackTrace();
					}
					request(1);
				}

				@Override
				public void onError(Throwable t) {
					try {
						getRemote()
								.sendString("{\"id\": \"" + id + "\", \"type\":\"error\", \"payload\": \"ERRROR!!!\"}");
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onComplete() {
					try {
						getRemote().sendString("{\"id\": \"" + id + "\", \"type\":\"complete\"}");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			System.out.println("Parameters is NULL for query: " + graphqlQuery);
		}
	}

	private void request(int n) {
		Subscription subscription = subscriptionRef.get();
		if (subscription != null) {
			subscription.request(n);
		}
	}
}