package com.csc.java.ai.langchain4j;

import com.csc.java.ai.langchain4j.assistant.MemoryChatAssistant;
import com.csc.java.ai.langchain4j.assistant.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptTest {


    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testSystemMessage() {
        String chat = separateChatAssistant.chat(5, "明天是幾號 no_think");
        System.out.println(chat);
    }

    @Autowired
    private MemoryChatAssistant memoryChatAssistant;

    @Test
    public void testUserMessage(){
        String answer = memoryChatAssistant.chat("i am alex");
        System.out.println(answer);

        String answer2 = memoryChatAssistant.chat("i am 19 years old");
        System.out.println(answer2);


        String answer3 = memoryChatAssistant.chat("do you know who i am");
        System.out.println(answer3);
    }


    @Test
    public void testUserMessage2(){
        String answer = separateChatAssistant.chat2(10,"i am alex");
        System.out.println(answer);

        String answer2 = separateChatAssistant.chat2(10, "i am 19 years old");
        System.out.println(answer2);


        String answer3 = separateChatAssistant.chat2(10, "do you know who i am");
        System.out.println(answer3);
    }

    @Test
    public void testuserInfo() {
        String username = "alex hong";
        int age = 18;

        String s = separateChatAssistant.chat3(11, "我是誰，我多大了", username, age);

        System.out.println(s);
    }
}
