package io.github.mangjoo.customer_agent_chat_service.chat.model;

import java.util.UUID;

public interface MessageBroker {
    void publish(ChatMessage chatMessage);

    void subscribe(String topic);

    void unsubscribe(String sessionId, UUID topic);
}
