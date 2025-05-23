package com.csc.java.ai.langchain4j.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class BlockResponse implements Serializable {
    private String event;
    private String messageId;
    private String taskId;
    private String id;
    private String conversationId;
    private String mode;
    private String answer;
    private Map<String, Map<String, String>> metadata;
    private Long createdAt;

}