package io.github.mangjoo.customer_agent_chat_service.global;

import io.github.mangjoo.customer_agent_chat_service.chat.infra.ChatRoomRepository;
import io.github.mangjoo.customer_agent_chat_service.exception.ErrorCode;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public class ChatSubscriptionInterceptor implements ChannelInterceptor {
    private final ChatRoomRepository chatRoomRepository;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            UUID roomId = extractRoomId(destination);

            boolean available = chatRoomRepository.isAvailable(roomId);
            if (!available) {
                throw new MangJooException(ErrorCode.BAD_REQUEST, "Chat room is not available");
            }
        }
        return message;
    }

    private UUID extractRoomId(String destination) {
        try {
            String[] parts = destination.split("/");
            String chatRoomId = parts[parts.length - 1];
            return UUID.fromString(chatRoomId);
        } catch (Exception e) {
            log.error("Fail to extract chat room ID from destination: {}", destination);
            throw new MangJooException(ErrorCode.BAD_REQUEST, "Fail to extract chat room ID from destination");
        }
    }
}
