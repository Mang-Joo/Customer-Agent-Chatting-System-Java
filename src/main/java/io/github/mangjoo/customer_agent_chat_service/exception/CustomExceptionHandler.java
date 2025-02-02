package io.github.mangjoo.customer_agent_chat_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MangJooException.class)
    public ResponseEntity<MangJooErrorResponse> handleMangJooException(MangJooException e) {
        log.error("MangJooException: {}", e.getMessage());
        return e.toErrorResponse();
    }
}
