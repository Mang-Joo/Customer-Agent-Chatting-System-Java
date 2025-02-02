package io.github.mangjoo.customer_agent_chat_service.chat;

import io.github.mangjoo.customer_agent_chat_service.chat.domain.ChatRoom;
import io.github.mangjoo.customer_agent_chat_service.chat.domain.RoomStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatRoomTest {

    @Test
    void room_status_is_waiting_when_create_room() {
        ChatRoom chatRoom = new ChatRoom(1L);
        assertNotNull(chatRoom);
        assertEquals(RoomStatus.WAITING, chatRoom.getRoomStatus());
    }

    @Test
    void if_customer_id_is_null_then_throw_exception() {
        assertThrows(IllegalArgumentException.class, () -> new ChatRoom(null));
    }

    @Test
    void room_status_is_connected_when_connect_room() {
        ChatRoom chatRoom = new ChatRoom(1L);
        ChatRoom connectedRoom = chatRoom.connect(2L);

        assertEquals(RoomStatus.CONNECTED, connectedRoom.getRoomStatus());
    }

    @Test
    void if_agent_is_already_connected_then_throw_exception() {
        ChatRoom chatRoom = new ChatRoom(1L);
        chatRoom.connect(2L);

        assertThrows(IllegalArgumentException.class, () -> chatRoom.connect(3L));
    }

    @Test
    void room_status_is_finished_when_finish_room() {
        ChatRoom chatRoom = new ChatRoom(1L);
        ChatRoom finishedRoom = chatRoom.finish();

        assertEquals(RoomStatus.FINISHED, finishedRoom.getRoomStatus());
    }
}