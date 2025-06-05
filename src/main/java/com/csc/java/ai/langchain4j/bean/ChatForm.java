package com.csc.java.ai.langchain4j.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Chat request form")
public class ChatForm {
    @Schema(description = "Memory ID for chat context", example = "1")
    @NotNull(message = "Memory ID is required")
    private Long memoryId;

    @Schema(description = "User message", example = "What is the status of my training?")
    @NotBlank(message = "Message cannot be empty")
    private String message;
}
