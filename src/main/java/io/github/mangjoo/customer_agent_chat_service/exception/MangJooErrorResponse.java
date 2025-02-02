package io.github.mangjoo.customer_agent_chat_service.exception;


import lombok.Getter;

@Getter
public class MangJooErrorResponse {
    private String errorCode;
    private String message;

    public MangJooErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
