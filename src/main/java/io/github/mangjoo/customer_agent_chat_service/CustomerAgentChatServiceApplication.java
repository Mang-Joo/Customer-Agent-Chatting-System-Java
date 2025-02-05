package io.github.mangjoo.customer_agent_chat_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
public class CustomerAgentChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerAgentChatServiceApplication.class, args);
    }

}
