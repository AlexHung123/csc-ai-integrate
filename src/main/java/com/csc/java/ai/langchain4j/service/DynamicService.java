package com.csc.java.ai.langchain4j.service;

import com.csc.java.ai.langchain4j.mybatis.core.mapper.DynamicMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DynamicService {

    @Autowired
    private DynamicMapper dynamicMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String executeQueryAsJson(String sqlQuery) {
        try {
            // Execute the dynamic query
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            // Convert the result to JSON
            if (result.isEmpty()) {
                return "[]"; // Return empty JSON array if no result
            }

            return objectMapper.writeValueAsString(result); // Serialize the result
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting result to JSON", e);
        }
    }
}
