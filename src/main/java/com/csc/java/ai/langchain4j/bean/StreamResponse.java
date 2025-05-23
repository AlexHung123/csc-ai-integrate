package com.csc.java.ai.langchain4j.bean;
import java.io.Serializable;
import lombok.Data;

@Data
public class StreamResponse implements Serializable {

    private String event;
    private String id;
    private String taskId;
    private String messageId;
    private String answer;
    private Long createdAt;
    private String conversationId;
}
