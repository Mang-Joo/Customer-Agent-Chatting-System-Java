package io.github.mangjoo.customer_agent_chat_service.member.model;

import io.github.mangjoo.customer_agent_chat_service.global.DateTemplate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends DateTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(columnDefinition = "boolean default false")
    private final boolean deleted = false;

    public Member(
            String email,
            String name,
            String password,
            MemberRole role
    ) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email is null");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is null");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password is null");
        }

        if (role == null) {
            throw new IllegalArgumentException("role is null");
        }

        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public static Member createUser(String email, String name, String password) {
        return new Member(
                email,
                name,
                password,
                MemberRole.USER
        );
    }

    public static Member createAgent(String email, String name, String password) {
        return new Member(
                email,
                name,
                password,
                MemberRole.AGENT
        );
    }

    public boolean isNotPasswordMatch(PasswordEncoder passwordEncoder, String password) {
        if (passwordEncoder == null) {
            throw new IllegalArgumentException("passwordEncoder is null");
        }

        return !passwordEncoder.matches(password, this.password);
    }

    public Member encryptPassword(PasswordEncoder passwordEncoder) {
        if (passwordEncoder == null) {
            throw new IllegalArgumentException("passwordEncoder is null");
        }
        this.password = passwordEncoder.encode(this.password);

        return this;
    }
}
