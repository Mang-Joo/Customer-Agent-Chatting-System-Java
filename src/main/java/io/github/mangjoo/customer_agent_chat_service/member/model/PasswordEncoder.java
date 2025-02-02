package io.github.mangjoo.customer_agent_chat_service.member.model;

public interface PasswordEncoder {
    String encode(String password);

    boolean matches(String rawPassword, String encodedPassword);
}
