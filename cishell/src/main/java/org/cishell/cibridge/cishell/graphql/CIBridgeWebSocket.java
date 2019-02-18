package org.cishell.cibridge.cishell.graphql;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import org.cishell.cibridge.cishell.util.JsonKit;
import org.cishell.cibridge.cishell.util.QueryParameters;
import org.cishell.cibridge.graphql.scalars.Scalars;
import org.cishell.cibridge.graphql.schema.CIBridgeSchema;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLSchema;

public class CIBridgeWebSocket extends WebSocketAdapter {

	private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();

	@Override
	@OnWebSocketConnect
	public void onWebSocketConnect(Session session) {
		System.out.println("session open");
		super.onWebSocketConnect(session);
		session.setIdleTimeout(10000);
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
		System.out.println("boom");
        cause.printStackTrace();
    }

	@Override
	public void onWebSocketText(String graphqlQuery) {
		System.out.println(graphqlQuery);
		try {
			this.getRemote().sendString("{\"type\":\"connection_ack\"}");
			Thread.sleep(1000);
			this.getRemote().sendString("{\"type\":\"data\", \"data\":{\"message\":\"hello world!!!!\"}}");
//			this.getSession().close();
//			System.out.println("closed session?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

//		QueryParameters parameters = QueryParameters.from(graphqlQuery);
//
//		System.out.println(parameters.getQuery());
//		ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(parameters.getQuery())
//				.variables(parameters.getVariables()).operationName(parameters.getOperationName()).build();
//
//		Instrumentation instrumentation = new ChainedInstrumentation(Collections.singletonList(new TracingInstrumentation()));
//
//		//
//		// In order to have subscriptions in graphql-java you MUST use the
//		// SubscriptionExecutionStrategy strategy.
//		//
//		
//		CIBridgeGraphQLSchemaProvider ciBridgeGraphQLSchemaProvider = new CIBridgeGraphQLSchemaProvider(EchoServlet.ciBridge);
//		
//		GraphQL graphQL = GraphQL.newGraphQL(ciBridgeGraphQLSchemaProvider.getSchema()).instrumentation(instrumentation)
//				.build();
//
//		ExecutionResult executionResult = graphQL.execute(executionInput);
//
//		Publisher<ExecutionResult> stockPriceStream = executionResult.getData();
//
//		stockPriceStream.subscribe(new Subscriber<ExecutionResult>() {
//
//			@Override
//			public void onSubscribe(Subscription s) {
//				subscriptionRef.set(s);
//				request(1);
//			}
//
//			@Override
//			public void onNext(ExecutionResult er) {
//				try {
//					Object stockPriceUpdate = er.getData();
//					getRemote().sendString(JsonKit.toJsonString(stockPriceUpdate));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				request(1);
//			}
//
//			@Override
//			public void onError(Throwable t) {
//				getSession().close();
//			}
//
//			@Override
//			public void onComplete() {
//				getSession().close();
//			}
//		});
	}

	private void request(int n) {
		Subscription subscription = subscriptionRef.get();
		if (subscription != null) {
			subscription.request(n);
		}
	}
}

//import java.io.IOException;
//
//import org.eclipse.jetty.websocket.api.RemoteEndpoint;
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
//import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//
//@WebSocket
//public class CIBridgeWebSocket {
//	private Session session;
//	private RemoteEndpoint remote;
//
//	public RemoteEndpoint getRemote() {
//		return remote;
//	}
//
//	@OnWebSocketClose
//	public void onClose(int statusCode, String reason) {
//		this.session = null;
//	}
//
//	@OnWebSocketConnect
//	public void onConnect(Session session) {
//		System.out.println("On connect");
//		this.session = session;
//		this.remote = session.getRemote();
//	}
//
//	@OnWebSocketMessage
//	public void onText(String message) {
//		System.out.println("on text");
//		if (session == null) {
//			// no connection, do nothing.
//			// this is possible due to async behavior
//			return;
//		}
//
//		try {
//			remote.sendString(message);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
