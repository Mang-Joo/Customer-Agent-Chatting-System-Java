package io.github.mangjoo.customer_agent_chat_service.chat.service;

import io.github.mangjoo.customer_agent_chat_service.chat.domain.ChatMessage;
import io.github.mangjoo.customer_agent_chat_service.chat.domain.ChatRoom;
import io.github.mangjoo.customer_agent_chat_service.chat.domain.MessageBroker;
import io.github.mangjoo.customer_agent_chat_service.chat.infra.ChatMessageRepository;
import io.github.mangjoo.customer_agent_chat_service.chat.infra.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageBroker messageBroker;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom createChatRoom(Long customerId) {
        log.info("Create chat room for customer ID: {}", customerId);
        ChatRoom saved = chatRoomRepository.save(new ChatRoom(customerId));
        log.info("Chat room created ID: {}", saved.getChatRoomId());
        return saved;
    }

    @Transactional
    public ChatRoom enterAgent(UUID chatRoomId, Long agentId) {
        log.info("Enter agent ID: {} to chat room ID: {}", agentId, chatRoomId);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId);
        return chatRoom.enterAgent(agentId);
    }

    public List<ChatRoom> getWaitingRooms() {
        log.info("Get waiting rooms");
        return chatRoomRepository.findAllByAgentIdIsNull();
    }

    public void sendMessage(ChatMessage message) {
        log.info("Send message to chat room ID: {}", message.getChatRoomId());
        messageBroker.publish(message);
        log.info("Message sent to chat room ID: {} sender ID : {}", message.getChatRoomId(), message.getSenderId());
    }

    public void leave(String sessionId, ChatMessage chatMessage) {
        final UUID roomId = chatMessage.getChatRoomId();

        log.info("Leave chat room ID: {}", roomId);
        messageBroker.unsubscribe(sessionId, roomId);

        ChatRoom chatRoom = endChatRoom(roomId);
        log.info("Chat room ID: {} is closed", chatRoom.getChatRoomId());
    }

    public ChatRoom endChatRoom(UUID chatRoomId) {
        log.info("End chat room ID: {}", chatRoomId);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId);
        ChatRoom finished = chatRoom.finish();
        return chatRoomRepository.save(finished);
    }

    public List<ChatMessage> subScribe(UUID chatRoomId) {
        log.info("Get already messages for chat room ID: {}", chatRoomId);
        messageBroker.subscribe(chatRoomId.toString());
        return chatMessageRepository.findAllByChatRoomId(chatRoomId);
    }
}
