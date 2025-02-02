package io.github.mangjoo.customer_agent_chat_service.member.api.request;

import io.github.mangjoo.customer_agent_chat_service.member.model.Member;

public record MemberRegisterResponse(
        Long id
) {
    public static MemberRegisterResponse from(Member registeredMember) {
        return new MemberRegisterResponse(registeredMember.getUserId());
    }
}
