package io.github.mangjoo.customer_agent_chat_service.member.api.request;

import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberRegisterRequest(
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password,
        @NotNull @NotBlank String name
) {
    public Member toMember() {
        return Member.createUser(email, password, name);
    }
}
