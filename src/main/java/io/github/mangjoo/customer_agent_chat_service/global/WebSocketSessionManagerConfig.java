package io.github.mangjoo.customer_agent_chat_service.global;

import io.github.mangjoo.customer_agent_chat_service.chat.infra.WebSocketSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Configuration
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public class WebSocketSessionManagerConfig implements WebSocketHandlerDecoratorFactory {
    private final WebSocketSessionRepository webSocketSessionRepository;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                webSocketSessionRepository.save(session.getId(), session);
                super.afterConnectionEstablished(session);
            }
        };
    }
}
