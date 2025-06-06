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
        chatMemoryProvider = "chatMemoryProviderITMS"
)
public interface ITMSAgent {

//    @SystemMessage("Please generate trainee cover description")
    @SystemMessage(fromResource = "itms-prompt-template.txt")
    @UserMessage("no_think {{message}}")
    String chat(@MemoryId Long memoryId, @V("message") String userMessage);
}
