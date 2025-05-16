package com.csc.java.ai.langchain4j;

import com.csc.java.ai.langchain4j.assistant.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ToolTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testCalculartorTools() {
        String chat = separateChatAssistant.chat(13, "1+2等於几，9的平方根是多少 no_think");

        System.out.println(chat);
    }
}
