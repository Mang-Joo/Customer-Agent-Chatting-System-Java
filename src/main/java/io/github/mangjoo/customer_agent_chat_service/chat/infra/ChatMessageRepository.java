package io.github.mangjoo.customer_agent_chat_service.chat.infra;

import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Transactional
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageJpaRepository.save(chatMessage);
    }

    @Transactional
    public List<ChatMessage> findAllByChatRoomId(UUID chatRoomId) {
        return chatMessageJpaRepository.findAllByChatRoomId(chatRoomId);
    }
}
