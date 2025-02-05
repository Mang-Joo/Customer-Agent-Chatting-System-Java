package io.github.mangjoo.customer_agent_chat_service.member.model;

public interface DuplicateLoginCheck {
    void login(Long id, String sessionId);

    void checkLogin(Long id, String sessionId);
}
