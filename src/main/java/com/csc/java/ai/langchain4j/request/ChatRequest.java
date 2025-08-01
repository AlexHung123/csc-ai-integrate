package com.csc.java.ai.langchain4j.request;

import com.csc.java.ai.langchain4j.entity.chat.Message;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {

    @NotEmpty(message = "对话消息不能为空")
    List<Message> messages;

    @NotEmpty(message = "传入的模型不能为空")
    private String model;

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 系统提示词
     */
    private String sysPrompt;

    /**
     * 是否开启流式对话
     */
    private Boolean stream = Boolean.TRUE;

    /**
     * 知识库id
     */
    private String kid;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 会话id
     */
    private Long sessionId;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 对话角色
     */
    private String role;

}

