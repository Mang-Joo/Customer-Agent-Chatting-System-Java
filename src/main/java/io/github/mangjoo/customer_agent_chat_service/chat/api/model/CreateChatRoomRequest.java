package io.github.mangjoo.customer_agent_chat_service.chat.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CreateChatRoomRequest", description = "Request object to create a chat room")
public record CreateChatRoomRequest(
        @NotNull(message = "Title must not be null")
        @NotBlank(message = "Title must not be blank")
        @Schema(description = "Title of the chat room", requiredMode = Schema.RequiredMode.REQUIRED, example = "MangJoo")
        String title
) {
}
