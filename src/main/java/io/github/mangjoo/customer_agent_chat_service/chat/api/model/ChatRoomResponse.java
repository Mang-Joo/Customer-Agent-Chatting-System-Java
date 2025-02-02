package io.github.mangjoo.customer_agent_chat_service.chat.api.model;

import io.github.mangjoo.customer_agent_chat_service.chat.domain.ChatRoom;

import java.util.UUID;

public record ChatRoomResponse(
        UUID roomId
) {
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(chatRoom.getChatRoomId());
    }
}
