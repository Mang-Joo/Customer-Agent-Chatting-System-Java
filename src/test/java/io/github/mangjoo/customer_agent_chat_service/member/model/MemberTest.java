package io.github.mangjoo.customer_agent_chat_service.member.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberTest {

    @Test
    void if_email_is_null_or_blank_then_throw_exception() {
        assertThrows(IllegalArgumentException.class, () -> Member.createUser(null, "name", "password"));
        assertThrows(IllegalArgumentException.class, () -> Member.createUser(" ", "name", "password"));
        assertThrows(IllegalArgumentException.class, () -> Member.createUser("", "name", "password"));
    }

    @Test
    void if_name_is_null_or_blank_then_throw_exception() {
        assertThrows(IllegalArgumentException.class, () -> Member.createUser("email", null, "password"));
        assertThrows(IllegalArgumentException.class, () -> Member.createUser("email", " ", "password"));
        assertThrows(IllegalArgumentException.class, () -> Member.createUser("email", "", "password"));
    }

    @Test
    void if_password_is_null_or_blank_then_throw_exception() {
        assertThrows(IllegalArgumentException.class, () -> Member.createUser("email", "name", null));
        assertThrows(IllegalArgumentException.class, () -> Member.createUser("email", "name", " "));
        assertThrows(IllegalArgumentException.class, () -> Member.createUser("email", "name", ""));
    }

    @Test
    void if_password_encoder_is_null_then_throw_exception() {
        Member member = Member.createUser("email", "name", "password");
        assertThrows(IllegalArgumentException.class, () -> member.isNotPasswordMatch(null, "password"));
    }

    @Test
    void when_encode_password_then_different_raw_password() {
        PasswordEncoder passwordEncoder = new MockEncoder();
        String password = "password";
        Member member = Member.createUser("email", "name", password)
                .encryptPassword(passwordEncoder);

        assertNotEquals(password, member.getPassword());
    }

    @Test
    void when_password_not_match_then_return_false() {
        PasswordEncoder passwordEncoder = new MockEncoder();
        Member member = Member.createUser("email", "name", "password")
                .encryptPassword(passwordEncoder);

        assertTrue(member.isNotPasswordMatch(passwordEncoder, "password12"));
    }

    static class MockEncoder implements PasswordEncoder {

        @Override
        public String encode(String password) {
            return password + "encoded";
        }

        @Override
        public boolean matches(String rawPassword, String encodedPassword) {
            return encodedPassword.equals(rawPassword + "encoded");
        }
    }
}
