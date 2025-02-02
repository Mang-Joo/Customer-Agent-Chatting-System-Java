package io.github.mangjoo.customer_agent_chat_service.chat.domain;

import io.github.mangjoo.customer_agent_chat_service.global.DateTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends DateTemplate {
    private static final Long DEFAULT_ID = 0L;

    @Id
    private UUID chatRoomId;
    private Long customerId;
    private Long agentId;
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    public ChatRoom(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("customerId is null");
        }

        this.chatRoomId = UUID.randomUUID();
        this.customerId = customerId;
        this.agentId = DEFAULT_ID;
        this.roomStatus = RoomStatus.WAITING;
    }

    public ChatRoom connect(Long agentId) {
        if (!Objects.equals(this.agentId, DEFAULT_ID)) {
            throw new IllegalArgumentException("Agent is already connected");
        }

        this.roomStatus = RoomStatus.CONNECTED;
        this.agentId = agentId;
        return this;
    }

    public ChatRoom finish() {
        this.roomStatus = RoomStatus.FINISHED;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(chatRoomId, chatRoom.chatRoomId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chatRoomId);
    }

    public ChatRoom enterAgent(Long agentId) {
        if (!Objects.equals(this.agentId, DEFAULT_ID)) {
            throw new IllegalArgumentException("Agent is already connected");
        }

        this.agentId = agentId;
        this.roomStatus = RoomStatus.CONNECTED;
        return this;
    }
}
