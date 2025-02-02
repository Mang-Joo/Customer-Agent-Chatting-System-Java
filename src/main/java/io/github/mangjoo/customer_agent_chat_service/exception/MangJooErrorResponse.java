package io.github.mangjoo.customer_agent_chat_service.exception;


public record MangJooErrorResponse(
        String errorCode,
        String message
) {
}
