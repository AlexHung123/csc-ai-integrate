package com.csc.java.ai.langchain4j.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

@Slf4j
public class SSEUtil {

    public static void sendErrorEvent(ResponseBodyEmitter sseEmitter, String errorMessage) {
        try {
            sseEmitter.send(errorMessage);
        } catch (IOException e) {
            log.error("SSE发送失败: {}", e.getMessage());
        }
        sseEmitter.complete();
    }
}
