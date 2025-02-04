package io.github.mangjoo.customer_agent_chat_service.security.login;

import io.github.mangjoo.customer_agent_chat_service.exception.ErrorCode;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.github.mangjoo.customer_agent_chat_service.member.model.PasswordEncoder;
import io.github.mangjoo.customer_agent_chat_service.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member login(MemberLogin login) {
        Member member = memberRepository.findByEmail(login.email());
        if (member.isNotPasswordMatch(passwordEncoder, login.password())) {
            throw new MangJooException(ErrorCode.UNAUTHORIZED, "Invalid email or password.");
        }
        return member;
    }
}
