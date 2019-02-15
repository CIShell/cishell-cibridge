package org.cishell.cibridge.cishell.graphql;
//
//import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
//import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
//import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
//
//public class CIBridgeWebSocketCreator implements WebSocketCreator {
//	private MyBinaryEchoSocket binaryEcho;
//    private MyEchoSocket textEcho;
//
//    public MyAdvancedEchoCreator()
//    {
//        // Create the reusable sockets
//        this.binaryEcho = new MyBinaryEchoSocket();
//        this.textEcho = new MyEchoSocket();
//    }
//	@Override
//	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class CIBridgeWebSocketCreator implements Runnable
{
    private TimeZone timezone;
    private Session session;

    @OnWebSocketConnect
    public void onOpen(Session session)
    {
        this.session = session;
        this.timezone = TimeZone.getTimeZone("UTC");
        new Thread(this).start();
    }

    @OnWebSocketClose
    public void onClose(int closeCode, String closeReasonPhrase)
    {
        this.session = null;
    }

    @Override
    public void run()
    {
        while (this.session != null)
        {
            try
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                dateFormat.setTimeZone(timezone);

                String timestamp = dateFormat.format(new Date());
                this.session.getRemote().sendString(timestamp);
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException | IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}