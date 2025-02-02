package io.github.mangjoo.customer_agent_chat_service.chat.infra;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class WebSocketSessionRepository {
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void save(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
    }

    public WebSocketSession findById(String sessionId) {
        return sessions.get(sessionId);
    }

    public void delete(String sessionId) {
        sessions.remove(sessionId);
    }
}
