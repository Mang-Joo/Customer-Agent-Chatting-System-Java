package io.github.mangjoo.customer_agent_chat_service.member.api;

import io.github.mangjoo.customer_agent_chat_service.member.api.request.MemberRegisterRequest;
import io.github.mangjoo.customer_agent_chat_service.member.api.request.MemberRegisterResponse;
import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.github.mangjoo.customer_agent_chat_service.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(
            @Valid @RequestBody MemberRegisterRequest request
    ) {
        Member registeredMember = memberService.register(request.toMember());
        return ResponseEntity.ok(MemberRegisterResponse.from(registeredMember));
    }
}
