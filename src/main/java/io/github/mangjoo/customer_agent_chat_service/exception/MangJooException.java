package io.github.mangjoo.customer_agent_chat_service.exception;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class MangJooException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public MangJooException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ResponseEntity<MangJooErrorResponse> toErrorResponse() {
        return ResponseEntity.status(errorCode.getCode())
                .body(new MangJooErrorResponse(errorCode.getStatusCode(), message));
    }
}
