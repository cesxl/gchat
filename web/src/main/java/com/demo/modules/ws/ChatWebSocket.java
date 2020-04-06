package com.demo.modules.ws;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chat Web Socket
 *
 * @author gc
 */
@Component
@ServerEndpoint(value = "/api/chat")
public class ChatWebSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocket.class);
    public static final Set<Session> SESSIONS = Collections.newSetFromMap(new ConcurrentHashMap());

    @OnOpen
    public void onConnect(final Session session) {
        SESSIONS.add(session);

        if (session.isOpen()) {
            session.getAsyncRemote().sendText("server reply Connect Success");
        }
    }

    @OnClose
    public void onClose(final Session session, final CloseReason closeReason) {
        removeSession(session);
        LOGGER.info("onClose：{} {} {}", session.getId(), closeReason.getCloseCode(), closeReason.getReasonPhrase());
    }

    @OnMessage
    public void onMessage(byte[] message) {
        try {
            LOGGER.info("OnMessage Binary：{}", byteToStr(ByteBuffer.wrap(message)));
            synchronized (SESSIONS) {
                for (Session session : SESSIONS) {
                    if (session.isOpen()) {
                        session.getAsyncRemote().sendBinary(ByteBuffer.wrap(message));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @OnMessage
    public void onMessage(Session session, final String message) {
        LOGGER.info("onMessage Text：{}", message);

        if (StringUtils.isNotBlank(message)) {
            synchronized (SESSIONS) {
                for (Session ses : SESSIONS) {
                    if (ses.isOpen()) {
                        ses.getAsyncRemote().sendText("reply " + message);
                        LOGGER.info("Send Msg：{}", message);
                    }
                }
            }
        }

    }

    @OnError
    public void onError(final Session session, final Throwable error) {
        removeSession(session);
        LOGGER.info("onError：{} {}", session.getId(), error.getMessage());
    }

    public static void notifyChat(final String message) {
        synchronized (SESSIONS) {
            for (Session session : SESSIONS) {
                if (session.isOpen()) {
                    LOGGER.info("Send Msg：{}", message);
                    session.getAsyncRemote().sendText(message);
                }
            }
        }
    }

    public String byteToStr(ByteBuffer byteBuffer) {
        Charset charset = null;
        CharsetDecoder characterDecoder = null;
        CharBuffer charBuffer = null;
        try {
            characterDecoder = StandardCharsets.UTF_8.newDecoder();
            charBuffer = characterDecoder.decode(byteBuffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (CharacterCodingException e) {
            return null;
        }
    }

    private void removeSession(final Session session) {
        LOGGER.info("remove session：{}", session.getId());
        SESSIONS.remove(session);
    }
}
