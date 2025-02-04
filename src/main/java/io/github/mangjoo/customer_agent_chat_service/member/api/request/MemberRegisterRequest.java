package io.github.mangjoo.customer_agent_chat_service.member.api.request;

import io.github.mangjoo.customer_agent_chat_service.member.model.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Register Member Request Object")
public record MemberRegisterRequest(
        @Schema(
                description = "Register Member Email (Login Email)",
                example = "MangJoo@gmail.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Email is required.")
        @NotBlank(message = "Email is required.")
        @Email(message = "It's not email form.")
        String email,

        @Schema(
                description = "Register Member Password",
                example = "MangJoo1234",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password is required.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "Password have to contain at least one letter and one number, and be longer than 8 characters."
        )
        String password,

        @Schema(
                description = "Register Member Name",
                example = "MangJoo",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull @NotBlank String name
) {
    public Member toMember() {
        return Member.createUser(email, name, password);
    }
}
