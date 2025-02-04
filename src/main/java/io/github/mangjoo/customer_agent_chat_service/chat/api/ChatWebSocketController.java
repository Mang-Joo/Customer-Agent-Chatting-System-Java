package io.github.mangjoo.customer_agent_chat_service.chat.api;

import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatMessage;
import io.github.mangjoo.customer_agent_chat_service.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final ChatService chatService;

    @MessageMapping("/chat/room/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable("chatRoomId") UUID chatRoomId,
            @Payload ChatController.ChatMessageRequest message,
            @Header("simpSessionId") String sessionId
    ) {
        ChatMessage chatMessage = message.toChatMessage(chatRoomId);
        if (chatMessage.isEndMessage()) {
            chatService.leave(sessionId, chatMessage);
        } else {
            chatService.sendMessage(chatMessage);
        }
    }

    @SubscribeMapping("/chat/room/{chatRoomId}")
    public List<ChatMessage> subscribeChatRoom(
            @DestinationVariable("chatRoomId") UUID chatRoomId
    ) {
        return chatService.subScribe(chatRoomId);
    }
}
