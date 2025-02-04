package io.github.mangjoo.customer_agent_chat_service.chat.api;

import io.github.mangjoo.customer_agent_chat_service.chat.api.model.ChatRoomResponse;
import io.github.mangjoo.customer_agent_chat_service.chat.api.model.CreateChatRoomRequest;
import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatMessage;
import io.github.mangjoo.customer_agent_chat_service.chat.model.ChatRoom;
import io.github.mangjoo.customer_agent_chat_service.chat.service.ChatService;
import io.github.mangjoo.customer_agent_chat_service.exception.MangJooErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Create Chat Room API",
            description = "Request agent to create counseling chat room",
            tags = {"Chat API"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Chat Room Created Successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Only for User", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Can't request of agent", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class)))
            }
    )
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody CreateChatRoomRequest request
    ) {
        ChatRoom chatRoom = chatService.createChatRoom(new ChatRoom(request.title(), memberId));
        return ResponseEntity.ok(ChatRoomResponse.from(chatRoom));
    }

    @PostMapping("/join")
    @PreAuthorize("hasRole('AGENT')")
    @Operation(
            summary = "Join Chat Room API",
            description = "Request agent to join counseling chat room",
            tags = {"Chat API"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Chat Room Joined Successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Only for Agent", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Can't request of user", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class)))
            }
    )
    public ResponseEntity<ChatRoomResponse> joinChatRoom(
            @AuthenticationPrincipal Long memberId,
            @RequestParam("chatRoomId") UUID chatRoomId
    ) {
        ChatRoom chatRoom = chatService.enterAgent(chatRoomId, memberId);
        return ResponseEntity.ok(ChatRoomResponse.from(chatRoom));
    }


    @GetMapping("/waiting-rooms")
    @PreAuthorize("hasRole('AGENT')")
    @Operation(
            summary = "Get Waiting Rooms API",
            description = "Get all waiting rooms",
            tags = {"Chat API"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Waiting Rooms Fetched Successfully"),
                    @ApiResponse(responseCode = "401", description = "Only for Agent", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Can't request of user", content = @Content(schema = @Schema(implementation = MangJooErrorResponse.class)))
            }
    )
    public ResponseEntity<List<ChatRoomResponse>> getWaitingRooms() {
        List<ChatRoom> chatRoom = chatService.getWaitingRooms();
        List<ChatRoomResponse> responses = chatRoom.stream().map(ChatRoomResponse::from).toList();

        return ResponseEntity.ok(responses);
    }

    public record ChatMessageRequest(
            Long senderId,
            String message
    ) {
        public ChatMessage toChatMessage(UUID chatRoomId) {
            return new ChatMessage(chatRoomId, senderId, message);
        }
    }

}
