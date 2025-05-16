package com.csc.java.ai.langchain4j;

import com.csc.java.ai.langchain4j.assistant.Assistant;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTest {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Test
    public void testChat(){
        Assistant assistant = AiServices.create(Assistant.class, openAiChatModel);
        String answer = assistant.chat("who are you no_think");
        System.out.println(answer);
    }

    @Autowired
    private Assistant assistant;

    @Test
    public void testChatV2(){
        String answer = assistant.chat("who are you no_think");
        System.out.println(answer);
    }


}
