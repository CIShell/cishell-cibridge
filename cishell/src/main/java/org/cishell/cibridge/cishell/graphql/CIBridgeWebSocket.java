package org.cishell.cibridge.cishell.graphql;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.reactivestreams.Subscription;


public class CIBridgeWebSocket extends WebSocketAdapter {

	private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();

	@Override
	@OnWebSocketConnect
	public void onWebSocketConnect(Session session) {
		System.out.println("session open");
		super.onWebSocketConnect(session);
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
	public void onWebSocketText(String graphqlQuery) {

//		QueryParameters parameters = QueryParameters.from(graphqlQuery);
//
//		ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(parameters.getQuery())
//				.variables(parameters.getVariables()).operationName(parameters.getOperationName()).build();
//
		
//		Instrumentation instrumentation = new ChainedInstrumentation(singletonList(new TracingInstrumentation()));
//
//		//
//		// In order to have subscriptions in graphql-java you MUST use the
//		// SubscriptionExecutionStrategy strategy.
//		//
//		GraphQL graphQL = GraphQL.newGraphQL(graphqlPublisher.getGraphQLSchema()).instrumentation(instrumentation)
//				.build();
//
//		ExecutionResult executionResult = graphQL.execute(executionInput);
//
//		Publisher<ExecutionResult> stockPriceStream = executionResult.getData();

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
//				log.debug("Sending stick price update");
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
//				log.error("Subscription threw an exception", t);
//				getSession().close();
//			}
//
//			@Override
//			public void onComplete() {
//				log.info("Subscription complete");
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
