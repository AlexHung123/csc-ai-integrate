package com.csc.java.ai.langchain4j.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class DifyRequestBody implements Serializable {

    private String query;
    private Map<String, String> inputs;

    @JSONField(name = "response_mode")
    private String responseMode;

    private String user;

    @JSONField(name = "conversation_id")
    private String conversationId;
}
