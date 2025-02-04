package io.github.mangjoo.customer_agent_chat_service.security.login;

public record LoginResponse(
        Long id,
        String name,
        String role
) {
}
