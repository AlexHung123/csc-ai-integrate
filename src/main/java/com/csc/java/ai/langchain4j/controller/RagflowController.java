package com.csc.java.ai.langchain4j.controller;

import com.csc.java.ai.langchain4j.bean.ChatForm;
import com.csc.java.ai.langchain4j.mapper.DynamicMapper;
import com.csc.java.ai.langchain4j.service.RagflowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ragflow")
public class RagflowController {

    @Autowired
    private RagflowService ragflowService;

    @Autowired
    private DynamicMapper dynamicMapper;

    private static final String AGENT_ID_AI_HEAD = "b7482380355311f0bc530242ac130006";

    private static final String AGENT_ID_TRAINING_GUIDE = "a41df9fc3f6111f0a7fb0242ac130006";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/stream")
    public String getStreamResponse(@RequestParam("question") String question) {
        try {
            String sqlQuery = ragflowService.getStreamData(question, AGENT_ID_AI_HEAD);
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            if (result.isEmpty()) {
                return "[]";
            }

            return objectMapper.writeValueAsString(result); // Serialize the result
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping(value = "/stream/training-guide", produces = "text/stream;charset=utf-8")
    public Flux<String> getStreamResponseForTrainingGuide(@RequestParam("message") String question) {
        try {
            return ragflowService.getFluxStreamData(question, AGENT_ID_TRAINING_GUIDE);
        } catch (Exception e) {
            return Flux.just("Error: " + e.getMessage());
        }
    }

    @GetMapping("/session")
    public String getSession(@RequestParam("agentId") String agentId) {
        try {
            return ragflowService.getSessionId(agentId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
