package io.github.mangjoo.customer_agent_chat_service.member.api;

import io.github.mangjoo.customer_agent_chat_service.exception.MangJooErrorResponse;
import io.github.mangjoo.customer_agent_chat_service.member.api.request.MemberRegisterRequest;
import io.github.mangjoo.customer_agent_chat_service.member.api.request.MemberRegisterResponse;
import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.github.mangjoo.customer_agent_chat_service.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Member API", description = "Member API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register Member API",
            description = "Register Member",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Member Registered Successfully"),
                    @ApiResponse(responseCode = "400", description = "Member Already Exists or Invalid Request",
                            content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<MemberRegisterResponse> register(
            @Parameter(description = "Register Member Request", required = true)
            @Valid @RequestBody MemberRegisterRequest request
    ) {
        Member registeredMember = memberService.register(request.toMember());
        return ResponseEntity.ok(MemberRegisterResponse.from(registeredMember));
    }
}
