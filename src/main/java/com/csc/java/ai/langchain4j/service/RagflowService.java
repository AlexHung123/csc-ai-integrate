package com.csc.java.ai.langchain4j.service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class RagflowService {

    @Value("${ragflow.api.host}")
    private String apiHost;

    @Value("${ragflow.api.key}")
    private String apiKey;

    @Value("${ragflow.agent.id}")
    private String agentId;

    private static final String API_HOST = "http://192.168.1.41:8001";
    private static final String API_KEY = "ragflow-c5OGU2NTRlMzU1OTExZjBhOGM5MDI0Mm";
    private static final String AGENT_ID = "b7482380355311f0bc530242ac130006";
//    private static final String QUESTION = "找出最早开始的培训班级";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSessionId() {
        String url = UriComponentsBuilder.fromHttpUrl(API_HOST)
                .path("/api/v1/agents/{agentId}/completions")
                .buildAndExpand(AGENT_ID)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", AGENT_ID);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("请求失败，状态码: " + response.getStatusCode());
            return null;
        }

        String responseBody = response.getBody();
        if (responseBody == null || responseBody.isEmpty()) {
            System.out.println("空响应体");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new java.io.ByteArrayInputStream(responseBody.getBytes())))) {

            return reader.lines()
                    .filter(line -> line != null && line.startsWith("data:"))
                    .map(line -> line.substring(5).trim())
                    .map(line -> {
                        try {
                            JSONObject jsonObject = JSON.parseObject(line);
                            if (jsonObject.containsKey("data") && jsonObject.get("data") instanceof JSONObject) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                if (dataObject.containsKey("session_id")) {
                                    return dataObject.getString("session_id");
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("解析失败: " + e.getMessage());
                        }
                        return null;
                    })
                    .filter(sessionId -> sessionId != null && !sessionId.isEmpty())
                    .findFirst()
                    .orElse(null);

        } catch (IOException e) {
            System.out.println("流处理失败: " + e.getMessage());
            return null;
        }
    }

    public String getStreamData(String question){
        String sessionId = getSessionId();
        if (sessionId == null){
            return "";
        }

        String url = UriComponentsBuilder.fromHttpUrl(API_HOST)
                .path("/api/v1/agents/{agentId}/completions")
                .buildAndExpand(AGENT_ID)
                .toUriString();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", AGENT_ID);
        requestBody.put("question", question);
        requestBody.put("stream", "true"); // Enable streaming
        requestBody.put("session_id", sessionId);

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String queryString = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJsonResponse) // Parse each chunk to extract the 'answer' field
                .filter(answer -> answer != null && !answer.isEmpty()) // Filter out empty or null answers
                .doOnNext(System.out::println) // Print each answer for debugging
                .takeUntil(answer -> answer.endsWith(";")) // Stop when the final SQL query is detected
                .last() // Get the last emitted value (final SQL query)
                .block();
        return queryString;
    }

    private String parseJsonResponse(String jsonResponse) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse); // Use Jackson ObjectMapper
            JsonNode dataNode = jsonNode.path("data");
            if (!dataNode.isMissingNode() && dataNode.has("answer")) {
                return dataNode.get("answer").asText().replace("```sql", "").replace("```", "").replace("<think>", "").replace("</think>","").trim();
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }
        return null;
    }

//    public static String extractQuery(String content) {
//        if (content == null || content.isEmpty()) {
//            return "";
//        }
//
//        int startIndex = content.indexOf("SELECT");
//        int endIndex = content.indexOf(";", startIndex);
//
//        if (startIndex != -1 && endIndex != -1) {
//            return content.substring(startIndex, endIndex + 1).trim();
//        }
//
//        return "";
//    }


//    public String getStreamData() {
//        String sessionId = getSessionId();
//        if (sessionId == null) {
//            return "";
//        }
//
//        String url = UriComponentsBuilder.fromHttpUrl(API_HOST)
//                .path("/api/v1/agents/{agentId}/completions")
//                .buildAndExpand(AGENT_ID)
//                .toUriString();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(API_KEY);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("id", AGENT_ID);
//        requestBody.put("question", QUESTION);
//        requestBody.put("stream", "false");
//        requestBody.put("session_id", sessionId);
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//
//        if (response.getStatusCode() != HttpStatus.OK) {
//            System.out.println("请求失败，状态码: " + response.getStatusCode());
//            return "";
//        }
//
//        String responseBody = response.getBody();
//        if (responseBody != null) {
//            System.out.println(responseBody);
//        }
//        return parseJsonResponse(responseBody);
//    }

//    private String parseJsonResponse(String jsonResponse) {
//        try {
//            JsonNode jsonNode = objectMapper.readTree(jsonResponse); // Use Jackson ObjectMapper
//            JsonNode dataNode = jsonNode.path("data");
//            if (!dataNode.isMissingNode() && dataNode.has("answer")) {
//                String answer = dataNode.get("answer").asText();
//                if (answer.endsWith(";")) { // Ensure it's the final query
//                    return answer;
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error parsing JSON response: " + e.getMessage());
//        }
//        return "";
//    }
}