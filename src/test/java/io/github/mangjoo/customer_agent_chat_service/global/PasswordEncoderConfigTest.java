package io.github.mangjoo.customer_agent_chat_service.global;

import io.github.mangjoo.customer_agent_chat_service.security.PasswordEncoderConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderConfigTest {

    @Test
    void password_encoder_match_test() {
        PasswordEncoderConfig passwordEncoderConfig = new PasswordEncoderConfig();
        PasswordEncoder passwordEncoder = passwordEncoderConfig.passwordEncoder();

        String mangjoo = passwordEncoder.encode("mangjoo");

        boolean matches = passwordEncoder.matches("mangjoo", mangjoo);

        assertTrue(matches);
    }
}