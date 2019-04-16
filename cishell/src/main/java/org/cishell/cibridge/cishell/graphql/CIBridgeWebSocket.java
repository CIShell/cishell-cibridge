package org.cishell.cibridge.cishell.graphql;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import org.cishell.cibridge.cishell.util.JsonKit;
import org.cishell.cibridge.cishell.util.QueryParameters;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

//todo check if the class is redundant, if not make sure its spec compliant
public class CIBridgeWebSocket extends WebSocketAdapter {

    private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();

    // Expected types of queries from client by server
    private static final String GQL_CONNECTION_INIT = "connection_init";
    private static final String GQL_START = "start";
    private static final String GQL_CONNECTION_TERMINATE = "connection_terminate";
    private static final String GQL_STOP = "stop";

    // Expected types of responses from server to client
    private static final String GQL_CONNECTION_ACK = "connection_ack";
    private static final String GQL_CONNECTION_ERROR = "connection_error";
    private static final String GQL_CONNECTION_KEEP_ALIVE = "ka";
    private static final String GQL_DATA = "data";
    private static final String GQL_ERROR = "error";
    private static final String GQL_COMPLETE = "complete";

    private Publisher<ExecutionResult> resultsStream;

    @Override
    @OnWebSocketConnect
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
//		session.setIdleTimeout(10000);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        Subscription subscription = subscriptionRef.get();
        if (subscription != null) {
            subscription.cancel();
        }
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        String response = generateResponseString(GQL_ERROR, cause, null);
        if (response != null) {
            try {
                this.getRemote().sendString(response);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid data given to generateResponseString method");
        }
        cause.printStackTrace();
    }

    @Override
    public void onWebSocketText(String graphqlQuery) {

        QueryParameters parameters = QueryParameters.from(graphqlQuery);
        switch (parameters.getType()) {
            case GQL_CONNECTION_INIT:
                try {
                    String ackResponse = generateResponseString(GQL_CONNECTION_ACK, null, parameters.getID());
                    String kaResponse = generateResponseString(GQL_CONNECTION_KEEP_ALIVE, null, parameters.getID());
                    if (ackResponse != null) {
                        this.getRemote().sendString(ackResponse);
                    } else {
                        System.out.println("Invalid data given to generateResponseString method");
                    }
                    if (kaResponse != null) {
                        this.getRemote().sendString(kaResponse);
                    } else {
                        System.out.println("Invalid data given to generateResponseString method");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case GQL_START:
                processSubscriptionQuery(parameters);
                break;
            case GQL_STOP:
                subscriptionRef.get().cancel();
                break;
            case GQL_CONNECTION_TERMINATE:
                getSession().close();
                break;
            default:
                System.out.println("Invalid Query Type: " + parameters.getType());
        }
    }

    private String generateResponseString(String type, Object payload, String id) {

        Map<String, Object> map = new HashMap<>();
        switch (type) {
            case GQL_CONNECTION_ACK:
            case GQL_CONNECTION_ERROR:
            case GQL_CONNECTION_KEEP_ALIVE:
            case GQL_COMPLETE:
                map.put("type", type);
                if (id != null) {
                    map.put("id", id);
                }
                if (payload != null) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("data", payload);
                    map.put("payload", dataMap);
                }
                break;
            case GQL_ERROR:
                map.put("type", type);
                if (id != null) {
                    map.put("id", id);
                }
                if (payload != null) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("error", payload);
                    map.put("payload", dataMap);
                }
                break;
            case GQL_DATA:
                map.put("type", type);
                if (id != null) {
                    map.put("id", id);
                }
                if (payload != null) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("data", payload);
                    map.put("payload", dataMap);
                }
                break;
        }
        if (map.size() > 0) {
            return JsonKit.toJsonString(map);
        } else {
            return null;
        }

    }

    public Publisher<ExecutionResult> getResultsStream() {
        return resultsStream;
    }

    private void processSubscriptionQuery(QueryParameters parameters) {

        String id = parameters.getID();

        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(parameters.getQuery())
                .variables(parameters.getVariables()).operationName(parameters.getOperationName()).build();

        ExecutionResult executionResult = CIBridgeSubscriptionServlet.graphql.execute(executionInput);

        if (executionResult.getErrors().size() > 0) {
            try {
                String response = generateResponseString(GQL_ERROR, executionResult.getErrors(), id);
                if (response != null) {
                    getRemote().sendString(response);
                } else {
                    System.out.println("Invalid data given to generateResponseString() method");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (executionResult.getData() instanceof LinkedHashMap) {
            try {
                getRemote().sendString(generateResponseString(GQL_DATA, executionResult.getData(), id));
                getRemote().sendString(generateResponseString(GQL_COMPLETE, null, id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resultsStream = executionResult.getData();
            resultsStream.subscribe(new Subscriber<ExecutionResult>() {

                @Override
                public void onSubscribe(Subscription s) {
                    subscriptionRef.set(s);
                    request(1);
                }

                @Override
                public void onNext(ExecutionResult er) {
                    try {
                        Object onDataChange = er.getData();
                        String response = generateResponseString(GQL_DATA, onDataChange, id);
                        if (response != null) {
                            getRemote().sendString(response);
                        } else {
                            System.out.println("Invalid data given to generateResponseString() method");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    request(1);
                }

                @Override
                public void onError(Throwable t) {
                    try {
                        String response = generateResponseString(GQL_ERROR, "Exception Occured while processing data", id);
                        if (response != null) {
                            getRemote().sendString(response);
                        } else {
                            System.out.println("Invalid data given to generateResponseString() method");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {
                    try {
                        String response = generateResponseString(GQL_COMPLETE, null, id);
                        if (response != null) {
                            getRemote().sendString(response);
                        } else {
                            System.out.println("Invalid data given to generateResponseString() method");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void request(int n) {
        Subscription subscription = subscriptionRef.get();
        if (subscription != null) {
            subscription.request(n);
        }
    }
}