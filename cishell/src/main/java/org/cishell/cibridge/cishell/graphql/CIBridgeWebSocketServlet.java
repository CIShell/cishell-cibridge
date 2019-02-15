package org.cishell.cibridge.cishell.graphql;

import org.cishell.cibridge.cishell.CIShellCIBridge;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class CIBridgeWebSocketServlet extends WebSocketServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void configure(WebSocketServletFactory factory) {

		System.out.println("Registering Socket");
		factory.register(CIBridgeWebSocket.class);
		System.out.println("Registering complete");

	}
}
