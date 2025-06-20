package com.csc.java.ai.langchain4j.service;

import com.csc.java.ai.langchain4j.request.ChatRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IChatService {

    SseEmitter chat(ChatRequest chatRequest, SseEmitter emitter);

    String getCategory();
}
