package io.github.mangjoo.customer_agent_chat_service.member.service;

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
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Member register(Member member) {
        log.info("Register member: {}", member.getEmail());
        if (memberRepository.existsByEmailAndName(member.getEmail())) {
            log.error("Member already exists: {}", member.getEmail());
            throw new MangJooException(ErrorCode.BAD_REQUEST, "Member already exists");
        }

        Member encryptPassword = member.encryptPassword(passwordEncoder);
        Member saved = memberRepository.save(encryptPassword);
        log.info("Member registered ID: {} E-Mail: {}", saved.getUserId(), saved.getEmail());
        return saved;
    }

    public Member findById(Long userId) {
        log.info("Find member by ID: {}", userId);
        return memberRepository.findById(userId);
    }

}
