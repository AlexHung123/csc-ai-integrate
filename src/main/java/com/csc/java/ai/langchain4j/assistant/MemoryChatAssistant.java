package com.csc.java.ai.langchain4j.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "openAiChatModel", chatMemory = "chatMemory")
public interface MemoryChatAssistant {

    @UserMessage("你是我的好朋友，請用粵語回答問題。{{it}}")
    String chat(String message);
}
