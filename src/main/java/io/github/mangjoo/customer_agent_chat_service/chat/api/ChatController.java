package io.github.mangjoo.customer_agent_chat_service.chat.api;

import io.github.mangjoo.customer_agent_chat_service.chat.api.model.ChatRoomResponse;
import io.github.mangjoo.customer_agent_chat_service.chat.domain.ChatMessage;
import io.github.mangjoo.customer_agent_chat_service.chat.domain.ChatRoom;
import io.github.mangjoo.customer_agent_chat_service.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @AuthenticationPrincipal Long memberId
    ) {
        ChatRoom chatRoom = chatService.createChatRoom(memberId);
        return ResponseEntity.ok(ChatRoomResponse.from(chatRoom));
    }

    @PostMapping("/join")
    public ResponseEntity<ChatRoomResponse> joinChatRoom(
            @AuthenticationPrincipal Long memberId,
            @RequestParam("chatRoomId") UUID chatRoomId
    ) {
        ChatRoom chatRoom = chatService.enterAgent(chatRoomId, memberId);
        return ResponseEntity.ok(ChatRoomResponse.from(chatRoom));
    }

    @SubscribeMapping("/chat/room/{chatRoomId}")
    public List<ChatMessage> subscribeChatRoom(
            @DestinationVariable("chatRoomId") UUID chatRoomId
    ) {
        return chatService.subScribe(chatRoomId);
    }

    @GetMapping("/waiting-rooms")
    public ResponseEntity<List<ChatRoomResponse>> getWaitingRooms() {
        List<ChatRoom> chatRoom = chatService.getWaitingRooms();
        List<ChatRoomResponse> responses = chatRoom.stream().map(ChatRoomResponse::from).toList();

        return ResponseEntity.ok(responses);
    }

    @MessageMapping("/chat/room/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable("chatRoomId") UUID chatRoomId,
            @Payload ChatMessageRequest message,
            @Header("simpSessionId") String sessionId
    ) {
        ChatMessage chatMessage = message.toChatMessage(chatRoomId);
        if (chatMessage.isEndMessage()) {
            chatService.leave(sessionId, chatMessage);
        } else {
            chatService.sendMessage(chatMessage);
        }
    }

    public record ChatMessageRequest(
            Long senderId,
            String message
    ) {
        public ChatMessage toChatMessage(UUID chatRoomId) {
            return new ChatMessage(chatRoomId, senderId, message);
        }
    }

}
