package com.csc.java.ai.langchain4j.controller;

import com.csc.java.ai.langchain4j.mapper.DynamicMapper;
import com.csc.java.ai.langchain4j.service.RagflowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ragflow")
public class RagflowController {

    @Autowired
    private RagflowService ragflowService;

    @Autowired
    private DynamicMapper dynamicMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/stream")
    public String getStreamResponse(@RequestParam("question") String question) {
        try {
//            String sessionId = ragflowService.getSessionId();
            String sqlQuery = ragflowService.getStreamData(question);
//            return sqlQuery;
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            // Convert the result to JSON
            if (result.isEmpty()) {
                return "[]"; // Return empty JSON array if no result
            }

            return objectMapper.writeValueAsString(result); // Serialize the result
//            return "";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/session")
    public String getSession() {
        try {
            return ragflowService.getSessionId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
