package com.csc.java.ai.langchain4j.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@Schema(description = "Dify API request body")
public class DifyRequestBody implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "User query", example = "What is the status of my training?")
    @NotBlank(message = "Query cannot be empty")
    private String query;

    @Schema(description = "Additional input parameters")
    @NotNull(message = "Inputs cannot be null")
    private Map<String, String> inputs;

    @Schema(description = "Response mode (streaming/blocking)", example = "streaming")
    @NotBlank(message = "Response mode cannot be empty")
    @JSONField(name = "response_mode")
    private String responseMode;

    @Schema(description = "User identifier", example = "user123")
    @NotBlank(message = "User identifier cannot be empty")
    private String user;

    @Schema(description = "Conversation identifier", example = "conv123")
    @JSONField(name = "conversation_id")
    private String conversationId;
}
