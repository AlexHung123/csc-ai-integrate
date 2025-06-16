package com.csc.java.ai.langchain4j.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RagflowService {
    private static final Logger logger = LoggerFactory.getLogger(RagflowService.class);
    private static final String SQL_QUERY_END_MARKER = ";";
    private static final String SQL_CODE_BLOCK_START = "```sql";
    private static final String SQL_CODE_BLOCK_END = "```";
    private static final String THINK_BLOCK_START = "<think>";
    private static final String THINK_BLOCK_END = "</think>";

    @Value("${ragflow.api.host}")
    private String apiHost;

    @Value("${ragflow.api.key}")
    private String apiKey;


    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public RagflowService(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getSessionId(String agentId) {
        String url = buildUrl(agentId, "/api/v1/agents/{agentId}/completions");
        HttpHeaders headers = createHeaders();
        Map<String, Object> requestBody = createRequestBody(agentId);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Request failed with status code: {}", response.getStatusCode());
                return null;
            }

            return extractSessionId(response.getBody());
        } catch (Exception e) {
            logger.error("Error getting session ID: {}", e.getMessage());
            return null;
        }
    }

    public String getStreamData(String question, String agentId) {
        String sessionId = getSessionId(agentId);
        if (sessionId == null) {
            return "";
        }

        String url = buildUrl(agentId, "/api/v1/agents/{agentId}/completions");
        Map<String, Object> requestBody = createStreamRequestBody(agentId, question, sessionId);

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        try {
            return webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToFlux(String.class)
                    .map(this::parseJsonResponse)
                    .filter(answer -> answer != null && !answer.isEmpty())
                    .takeUntil(answer -> answer.endsWith(SQL_QUERY_END_MARKER))
                    .last()
                    .block();
        } catch (Exception e) {
            logger.error("Error getting stream data: {}", e.getMessage());
            return "";
        }
    }

    public Flux<String> getFluxStreamData(String question, String agentId) {
        String sessionId = getSessionId(agentId);
        if (sessionId == null) {
            return Flux.empty();
        }

        String url = buildUrl(agentId, "/api/v1/agents/{agentId}/completions");
        Map<String, Object> requestBody = createStreamRequestBody(agentId, question + " no_think", sessionId);

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .map(chunk -> {
                    String cleanedChunk = chunk.replaceAll("##\\d+\\$\\$", "").trim();
                    return "responseData:" + cleanedChunk + "\n";
                });
    }

    private String parseJsonResponse(String jsonResponse) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            JsonNode dataNode = jsonNode.path("data");
            if (!dataNode.isMissingNode() && dataNode.has("answer")) {
                String answer = dataNode.get("answer").asText();
                return cleanSqlQuery(answer);
            }
        } catch (Exception e) {
            logger.error("Error parsing JSON response: {}", e.getMessage());
        }
        return null;
    }

    private String cleanSqlQuery(String query) {
        return query.replace(SQL_CODE_BLOCK_START, "")
                .replace(SQL_CODE_BLOCK_END, "")
                .replace(THINK_BLOCK_START, "")
                .replace(THINK_BLOCK_END, "")
                .trim();
    }

    private String extractSessionId(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            logger.error("Empty response body");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new java.io.ByteArrayInputStream(responseBody.getBytes())))) {
            return reader.lines()
                    .filter(line -> line != null && line.startsWith("data:"))
                    .map(line -> line.substring(5).trim())
                    .map(this::parseSessionIdFromJson)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            logger.error("Stream processing failed: {}", e.getMessage());
            return null;
        }
    }

    private Optional<String> parseSessionIdFromJson(String jsonLine) {
        try {
            JSONObject jsonObject = JSON.parseObject(jsonLine);
            if (jsonObject.containsKey("data") && jsonObject.get("data") instanceof JSONObject) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                if (dataObject.containsKey("session_id")) {
                    return Optional.of(dataObject.getString("session_id"));
                }
            }
        } catch (Exception e) {
            logger.error("JSON parsing failed: {}", e.getMessage());
        }
        return Optional.empty();
    }

    private String buildUrl(String agentId, String path) {
        return UriComponentsBuilder.fromHttpUrl(apiHost)
                .path(path)
                .buildAndExpand(agentId)
                .toUriString();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private Map<String, Object> createRequestBody(String agentId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", agentId);
        return requestBody;
    }

    private Map<String, Object> createStreamRequestBody(String agentId, String question, String sessionId) {
        Map<String, Object> requestBody = createRequestBody(agentId);
        requestBody.put("id", agentId);
        requestBody.put("question", question);
        requestBody.put("stream", "true");
        requestBody.put("session_id", sessionId);
        return requestBody;
    }
}