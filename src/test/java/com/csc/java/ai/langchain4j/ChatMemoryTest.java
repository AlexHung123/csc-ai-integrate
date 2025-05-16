package com.csc.java.ai.langchain4j;

import com.csc.java.ai.langchain4j.assistant.Assistant;
import com.csc.java.ai.langchain4j.assistant.MemoryChatAssistant;
import com.csc.java.ai.langchain4j.assistant.SeparateChatAssistant;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.spring.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;


@SpringBootTest
public class ChatMemoryTest {

    @Autowired
    private Assistant assistant;

    @Test
    public void testChatMemory() {
        String iAmAlex = assistant.chat("I am alex");
        System.out.println(iAmAlex);

        String whoAmI = assistant.chat("who am i");
        System.out.println(whoAmI);
    }

    @Autowired
    private OpenAiChatModel openAiChatModel;


    @Test
    public void testChatMemoryV2() {

        UserMessage userMessage1 = UserMessage.userMessage("I am alex no_think");
        ChatResponse chatResponse1 = openAiChatModel.chat(userMessage1);
        AiMessage aiMessage1 = chatResponse1.aiMessage();
        System.out.println(aiMessage1.text());

        UserMessage userMessage2 = UserMessage.userMessage("who am i no_think");
        ChatResponse chatResponse2 = openAiChatModel.chat(Arrays.asList(userMessage1, aiMessage1, userMessage2));
        AiMessage aiMessage2 = chatResponse2.aiMessage();
        System.out.println(aiMessage2.text());
    }


    @Test
    public void testChatMemory3(){
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(10);

        Assistant assistant1 = AiServices
                .builder(Assistant.class)
                .chatLanguageModel(openAiChatModel)
                .chatMemory(messageWindowChatMemory)
                .build();

        String answer1 = assistant1.chat("i am alex no_think");
        System.out.println(answer1);


        String answer2 = assistant1.chat("who am i no_think");
        System.out.println(answer2);

    }


    @Autowired
    private MemoryChatAssistant memoryChatAssistant;

    @Test
    public void testChatMemory4(){


        String answer1 = memoryChatAssistant.chat("i am alex no_think");
        System.out.println(answer1);


        String answer2 = memoryChatAssistant.chat("who am i no_think");
        System.out.println(answer2);

    }


    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testChatMemory5(){


        String answer1 = separateChatAssistant.chat(1,"i am alex no_think");
        System.out.println(answer1);


        String answer2 = separateChatAssistant.chat(1,"who am i no_think");
        System.out.println(answer2);

        String answer3 = separateChatAssistant.chat(2,"who am i no_think");
        System.out.println(answer3);

    }
}
