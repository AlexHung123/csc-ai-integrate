package com.csc.java.ai.langchain4j.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "ollamaChatModel",
        chatMemoryProvider = "chatBIMemoryProviderITMS"
)
public interface ITMSChatBIAgent {
    @SystemMessage("請針對用戶輸入的問題 {{message}} 和 響應内容 {{answer}} 進行格式美化，不需要額外添加其他元素")
    @UserMessage("no_think {{message}}")
    String chat(@MemoryId Long memoryId, @V("message") String userMessage, @V("answer") String responseAnswer);
}
