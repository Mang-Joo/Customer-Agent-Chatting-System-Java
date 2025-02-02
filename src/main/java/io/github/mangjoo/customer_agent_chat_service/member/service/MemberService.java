package io.github.mangjoo.customer_agent_chat_service.member.service;

import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.github.mangjoo.customer_agent_chat_service.member.model.PasswordEncoder;
import io.github.mangjoo.customer_agent_chat_service.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Member register(Member member) {
        log.info("Register member: {}", member.getEmail());
        Member encryptPassword = member.encryptPassword(passwordEncoder);
        Member saved = memberRepository.save(encryptPassword);
        log.info("Member registered ID: {} E-Mail: {}", saved.getUserId(), saved.getEmail());
        return saved;
    }

}
