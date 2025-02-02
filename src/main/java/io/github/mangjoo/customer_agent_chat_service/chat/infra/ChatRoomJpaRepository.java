package io.github.mangjoo.customer_agent_chat_service.chat.infra;

import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ChatRoom c where c.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findByChatRoomId(UUID chatRoomId);

    @Query("select c from ChatRoom c where c.agentId = 0")
    List<ChatRoom> findAllByAgentIdIsNull();

    Boolean existsByChatRoomId(UUID chatRoomId);
}
