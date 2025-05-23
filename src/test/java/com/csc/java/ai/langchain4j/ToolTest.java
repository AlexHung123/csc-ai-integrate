package com.csc.java.ai.langchain4j;

import com.csc.java.ai.langchain4j.assistant.ITMSAgent;
import com.csc.java.ai.langchain4j.assistant.SeparateChatAssistant;
import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.dto.TrainingHistoryDTO;
import com.csc.java.ai.langchain4j.service.CIDProfileSnapshotsService;
import com.csc.java.ai.langchain4j.service.TrainingHistoryResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ToolTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testCalculartorTools() {
        String chat = separateChatAssistant.chat(14, "1+2等於几，9的平方根是多少 no_think");

        System.out.println(chat);
    }

    @Test
    public void testGenerateTraineeProfileTools() {
        String chat = separateChatAssistant.chat(15, "Creating formal English descriptions for trainee profiles:CS943939 no_think");

        System.out.println(chat);
    }
}
