package com.demo.modules.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.net.URI;

/**
 * websocket客户端连接
 *
 * @author gc
 */
@ClientEndpoint
public class WebSocketClient {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketClient.class);

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        logger.info("======= onOpen ======");
    }

    @OnClose
    public void onClose() {
        logger.info("======= onClose ======");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("======= onMessage ====== {}",message);
    }

    @OnError
    public void onError(Throwable thr) {
        logger.info("======= onError ======");
    }

    public WebSocketClient() {
        super();
    }

    public WebSocketClient(String uri) {
        try {
            logger.info("Connecting to {}", uri);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(WebSocketClient.class, URI.create(uri));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 测试信息接收
     *
     * @param args
     */
//    public static void main(String[] args) {
//        String uri = "ws://localhost:8087/api/EbServer";//EbServer
//        WebSocketClient client = new WebSocketClient(uri);
//        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
//        try {
//            String params = "{UCMD_CTRL,xx,88,1,8}";
//            client.getSession().getBasicRemote().sendBinary(ByteBuffer.wrap("O".getBytes()));
//            client.getSession().getBasicRemote().sendText(params);
//            while (true) {
//                String line = r.readLine();
//                if (line.equals("quit")) {
//                    client.getSession().close();
//                    break;
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

}

