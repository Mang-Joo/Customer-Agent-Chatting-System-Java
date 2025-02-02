package io.github.mangjoo.customer_agent_chat_service.chat.infra;

import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatRoom;
import io.github.mangjoo.customer_agent_chat_service.exception.ErrorCode;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Transactional
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(chatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoom findById(UUID id) {
        return chatRoomJpaRepository
                .findByChatRoomId(id)
                .orElseThrow(() -> new MangJooException(ErrorCode.BAD_REQUEST, "ChatRoom not found"));
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> findAllByAgentIdIsNull() {
        return chatRoomJpaRepository.findAllByAgentIdIsNull();
    }

    @Transactional(readOnly = true)
    public boolean isAvailable(UUID chatRoomId) {
        return chatRoomJpaRepository.existsByChatRoomId(chatRoomId);
    }
}
