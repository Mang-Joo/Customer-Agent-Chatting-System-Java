package io.github.mangjoo.customer_agent_chat_service.global;

import io.github.mangjoo.customer_agent_chat_service.chat.infra.ChatRoomRepository;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatSubscriptionInterceptorTest {
    private final ChatRoomRepository roomRepository = mock(ChatRoomRepository.class);
    private final ChatSubscriptionInterceptor chatSubscriptionInterceptor = new ChatSubscriptionInterceptor(roomRepository);

    @Test
    void return_the_message_when_accessor_is_null() {
        //given
        Message<String> payload = new GenericMessage<>("payload");
        MessageChannel mockChannel = mock(MessageChannel.class);

        //when
        Message<?> message = chatSubscriptionInterceptor.preSend(payload, mockChannel);

        //then
        assertEquals(payload, message);
    }

    @Test
    void return_the_message_when_accessor_is_not_subscribe() {
        //given
        MessageChannel mockChannel = mock(MessageChannel.class);
        Message<String> message = new GenericMessage<>("payload");
        StompHeaderAccessor mockAccessor = mock(StompHeaderAccessor.class);
        when(mockAccessor.getCommand()).thenReturn(StompCommand.CONNECT);

        //when
        Message<?> result = chatSubscriptionInterceptor.preSend(message, mockChannel);

        //then
        assertEquals(message, result);
        verify(roomRepository, never()).isAvailable(any());
    }

    @Test
    void throw_exception_when_fail_to_extract_chat_room_id() {
        //given
        MessageChannel mockChannel = mock(MessageChannel.class);
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        stompHeaderAccessor.setDestination("/sub/chat/room/invalid");
        Message<String> message = new GenericMessage<>("payload", stompHeaderAccessor.getMessageHeaders());

        //then
        assertThrows(MangJooException.class, () -> chatSubscriptionInterceptor.preSend(message, mockChannel));
    }

    @Test
    void check_availability_chat_room() {
        //given
        MessageChannel mockChannel = mock(MessageChannel.class);
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        stompHeaderAccessor.setDestination("/sub/chat/room/123e4567-e89b-12d3-a456-426614174000");
        Message<String> message = new GenericMessage<>("payload", stompHeaderAccessor.getMessageHeaders());

        //when
        when(roomRepository.isAvailable(any())).thenReturn(true);
        Message<?> predSend = chatSubscriptionInterceptor.preSend(message, mockChannel);

        //then
        assertEquals(message, predSend);
    }

    @Test
    void throw_exception_when_not_exists_room() {
        //given
        MessageChannel mockChannel = mock(MessageChannel.class);
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        stompHeaderAccessor.setDestination("/sub/chat/room/123e4567-e89b-12d3-a456-426614174000");
        Message<String> message = new GenericMessage<>("payload", stompHeaderAccessor.getMessageHeaders());
        when(roomRepository.isAvailable(any())).thenReturn(false);

        //then
        assertThrows(MangJooException.class, () -> chatSubscriptionInterceptor.preSend(message, mockChannel));
    }
}