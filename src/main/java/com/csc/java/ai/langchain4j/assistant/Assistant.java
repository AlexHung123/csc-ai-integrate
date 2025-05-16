package com.csc.java.ai.langchain4j.assistant;


import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

//@AiService
@AiService(wiringMode = EXPLICIT, chatModel = "openAiChatModel")
public interface Assistant {

    String chat(String userMessage);
}
