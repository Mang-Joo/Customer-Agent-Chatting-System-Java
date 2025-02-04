package io.github.mangjoo.customer_agent_chat_service.member.repository;

import io.github.mangjoo.customer_agent_chat_service.exception.ErrorCode;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.github.mangjoo.customer_agent_chat_service.member.model.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberJpaRepository
                .findByEmail(email)
                .orElseThrow(() -> new MangJooException(ErrorCode.BAD_REQUEST, "Member not found"));
    }

    @Transactional
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmailAndName(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member findById(Long userId) {
        return memberJpaRepository.findById(userId)
                .orElseThrow(() -> new MangJooException(ErrorCode.BAD_REQUEST, "Member not found"));
    }
}
