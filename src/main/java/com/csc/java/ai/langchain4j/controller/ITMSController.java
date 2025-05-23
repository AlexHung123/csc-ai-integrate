package com.csc.java.ai.langchain4j.controller;

import com.csc.java.ai.langchain4j.assistant.ITMSAgent;
import com.csc.java.ai.langchain4j.bean.ChatForm;
import com.csc.java.ai.langchain4j.tools.GenerateTraineeProfileTools;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ITMS - AI")
@RestController
@RequestMapping("/ai/itms")
public class ITMSController {

    @Autowired
    private ITMSAgent itmsAgent;

    @Autowired
    private GenerateTraineeProfileTools generateTraineeProfileTools;

    @Operation(summary = "chat")
    @PostMapping("/chat")
    public String chat(@RequestBody ChatForm chatForm) {
        return itmsAgent.chat(chatForm.getMemoryId(), generateTraineeProfileTools.generateTraineeProfileEnglishCoverDescription("CS943939"));
    }
}
