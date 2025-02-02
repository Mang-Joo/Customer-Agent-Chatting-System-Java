package io.github.mangjoo.customer_agent_chat_service.chat.infra;

import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select c from ChatMessage c where c.chatRoomId = :chatRoomId order by c.createdAt")
    List<ChatMessage> findAllByChatRoomId(@Param("chatRoomId") UUID chatRoomId);
}
