package io.github.mangjoo.customer_agent_chat_service.chat.model;

import io.github.mangjoo.customer_agent_chat_service.global.DateTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends DateTemplate {
    private static final String END_MESSAGE = "종료";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private UUID chatRoomId;
    private Long senderId;
    private String message;

    public ChatMessage(UUID chatRoomId, Long senderId, String message) {
        if (chatRoomId == null) {
            throw new IllegalArgumentException("chatRoomId is null");
        }

        if (senderId == null) {
            throw new IllegalArgumentException("senderId is null");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message is null");
        }

        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
    }

    public static ChatMessage endMessage(UUID chatRoomId) {
        return new ChatMessage(chatRoomId, 0L, END_MESSAGE);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(messageId);
    }

    public boolean isEndMessage() {
        return message.equals(END_MESSAGE);
    }
}
