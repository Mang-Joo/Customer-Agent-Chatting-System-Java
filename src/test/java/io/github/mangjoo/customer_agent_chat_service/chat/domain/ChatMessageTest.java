package io.github.mangjoo.customer_agent_chat_service.chat.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatMessageTest {

    @Test
    void should_throw_exception_if_room_id_is_null_when_create() {
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage(null, 1L, "Hello"));
    }

    @Test
    void should_throw_exception_if_sender_id_is_null_when_create() {
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage(UUID.randomUUID(), null, "Hello"));
    }

    @Test
    void should_throw_exception_if_message_is_null_or_blank_when_create() {
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage(UUID.randomUUID(), 1L, null));
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage(UUID.randomUUID(), 1L, ""));
    }

    @Test
    void return_true_when_message_is_end() {
        ChatMessage chatMessage = new ChatMessage(UUID.randomUUID(), 1L, "종료");
        assertTrue(chatMessage.isEndMessage());
    }
}