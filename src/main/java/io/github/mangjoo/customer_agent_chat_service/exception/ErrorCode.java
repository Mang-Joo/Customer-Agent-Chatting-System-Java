package io.github.mangjoo.customer_agent_chat_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(400, "C001", "Bad Request"),
    UNAUTHORIZED(401, "C002", "Unauthorized"),
    FORBIDDEN(403, "C003", "Forbidden"),
    NOT_FOUND(404, "C004", "Not Found"),
    DB_ERROR(500, "C005", "Database Error"),
    INTERNAL_SERVER_ERROR(500, "C006", "Internal Server Error"),
    ;

    private final Integer code;
    private final String statusCode;
    private final String message;
}
