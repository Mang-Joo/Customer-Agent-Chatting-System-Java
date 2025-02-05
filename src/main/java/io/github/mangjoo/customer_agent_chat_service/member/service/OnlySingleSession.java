package io.github.mangjoo.customer_agent_chat_service.member.service;

import io.github.mangjoo.customer_agent_chat_service.exception.ErrorCode;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import io.github.mangjoo.customer_agent_chat_service.member.model.DuplicateLoginCheck;
import io.github.mangjoo.customer_agent_chat_service.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnlySingleSession implements DuplicateLoginCheck {
    private static final String SESSION_PREFIX = "duplicate:user:";
    private final MemberRepository memberRepository;


    @Override
    public void login(Long id, String sessionId) {
        memberRepository.saveSession(SESSION_PREFIX + id, sessionId);
    }

    @Override
    public void checkLogin(Long id, String sessionId) {
        String session = memberRepository.getSession(SESSION_PREFIX + id);
        if (session == null) {
            throw new MangJooException(ErrorCode.UNAUTHORIZED, "Session expired");
        }

        if (!session.equals(sessionId)) {
            throw new MangJooException(ErrorCode.UNAUTHORIZED, "Duplicate login detected");
        }

        memberRepository.renewSession(SESSION_PREFIX + id);
    }
}
