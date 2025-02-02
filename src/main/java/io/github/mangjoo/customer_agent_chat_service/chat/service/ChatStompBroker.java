package io.github.mangjoo.customer_agent_chat_service.chat.service;

import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatMessage;
import io.github.mangjoo.customer_agent_chat_service.chat.model.MessageBroker;
import io.github.mangjoo.customer_agent_chat_service.chat.infra.ChatMessageRepository;
import io.github.mangjoo.customer_agent_chat_service.chat.infra.WebSocketSessionRepository;
import io.github.mangjoo.customer_agent_chat_service.exception.ErrorCode;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatStompBroker implements MessageBroker {
    private static final String TOPIC_PREFIX = "/sub/chat/room/";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final WebSocketSessionRepository sessionRepository;

    @Override
    public void publish(ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        simpMessagingTemplate.convertAndSend(TOPIC_PREFIX + chatMessage.getChatRoomId(), savedMessage);
    }

    @Override
    public void subscribe(String topic) {
        log.info("Subscribe to topic: {}", topic);
    }

    @Override
    public void unsubscribe(String sessionId, UUID topic) {
        log.info("Unsubscribe from topic: {}", topic);
        try {
            WebSocketSession webSocketSession = sessionRepository.findById(sessionId);

            if (webSocketSession != null && webSocketSession.isOpen()) {
                ChatMessage closeMessage = ChatMessage.endMessage(topic);
                publish(closeMessage);

                webSocketSession.close();
                sessionRepository.delete(sessionId);

                log.info("Close session in room {}", topic);
            }
        } catch (Exception e) {
            log.error("Fail to close session", e);
            throw new MangJooException(ErrorCode.INTERNAL_SERVER_ERROR, "Fail to close session");
        }
    }
}
