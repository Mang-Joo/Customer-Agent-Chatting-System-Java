package io.github.mangjoo.customer_agent_chat_service.member.repository;

import io.github.mangjoo.customer_agent_chat_service.exception.ErrorCode;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooException;
import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.github.mangjoo.customer_agent_chat_service.member.model.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final RedisTemplate<Object, Object> redisTemplate;

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

    @Transactional
    public void saveSession(String sessionKey, String sessionValue) {
        redisTemplate.opsForHash().put(sessionKey, sessionKey, sessionValue);
        redisTemplate.expire(sessionKey, Duration.ofMinutes(30));
    }

    @Transactional(readOnly = true)
    public String getSession(String sessionKey) {
        return (String) redisTemplate.opsForHash().get(sessionKey, sessionKey);
    }

    @Transactional
    public void renewSession(String sessionKey) {
        redisTemplate.expire(sessionKey, Duration.ofMinutes(30));
    }

}
