package com.csc.java.ai.langchain4j;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LLMTest {

    @Test
    public void testQWENDemo(){

        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey("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")
                .modelName("qwen/qwen3-235b-a22b:free")
                .build();
        String answer = model.chat("which model do you base on?");
        System.out.println(answer);
    }
    
    @Autowired
    private OpenAiChatModel openAiChatModel;
    
    @Test
    public void testSpringboot(){
        String answer = openAiChatModel.chat("who ary you no_think");
        System.out.println(answer);
    }

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Test
    public void testSpringbootV2(){
        String answer = ollamaChatModel.chat("who ary you no_think");
        System.out.println(answer);
    }
}
