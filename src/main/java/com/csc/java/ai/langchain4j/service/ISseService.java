package com.csc.java.ai.langchain4j.service;

import com.csc.java.ai.langchain4j.request.ChatRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ISseService {

    SseEmitter sseChat(ChatRequest chatRequest, HttpServletRequest request);
}
