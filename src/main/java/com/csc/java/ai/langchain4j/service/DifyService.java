package com.csc.java.ai.langchain4j.service;

import com.alibaba.fastjson2.JSON;
import java.util.HashMap;
import java.util.List;

import com.csc.java.ai.langchain4j.bean.BlockResponse;
import com.csc.java.ai.langchain4j.bean.DifyRequestBody;
import com.csc.java.ai.langchain4j.bean.StreamResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Service
@RequiredArgsConstructor
public class DifyService {
    private static final Logger logger = LoggerFactory.getLogger(DifyService.class);
    private static final String STREAMING_MODE = "streaming";
    private static final String BLOCKING_MODE = "blocking";

    @Value("${dify.url}")
    private String url;

    private final RestTemplate restTemplate;

    private final WebClient webClient;

    public Flux<StreamResponse> streamingMessage(String query, Long userId, String apiKey) {
        try {
            DifyRequestBody body = createRequestBody(query, userId, STREAMING_MODE);
            return webClient.post()
                    .uri(url)
                    .headers(httpHeaders -> configureHeaders(httpHeaders, apiKey))
                    .bodyValue(JSON.toJSONString(body))
                    .retrieve()
                    .bodyToFlux(StreamResponse.class)
                    .doOnError(error -> logger.error("Error in streaming message: {}", error.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to process streaming message: {}", e.getMessage());
            return Flux.error(e);
        }
    }


    public BlockResponse blockingMessage(String query, Long userId, String apiKey) {
        try {
            DifyRequestBody body = createRequestBody(query, userId, BLOCKING_MODE);
            HttpHeaders headers = new HttpHeaders();
            configureHeaders(headers, apiKey);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(body), headers);
            ResponseEntity<BlockResponse> response = restTemplate.postForEntity(url, entity, BlockResponse.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Received non-OK status code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to get response from Dify service");
            }

            return response.getBody();
        } catch (Exception e) {
            logger.error("Failed to process blocking message: {}", e.getMessage());
            throw new RuntimeException("Error processing blocking message", e);
        }
    }

    private DifyRequestBody createRequestBody(String query, Long userId, String responseMode) {
        DifyRequestBody body = new DifyRequestBody();
        body.setInputs(new HashMap<>());
        body.setQuery(query);
        body.setResponseMode(responseMode);
        body.setConversationId("");
        body.setUser(userId.toString());
        return body;
    }

    private void configureHeaders(HttpHeaders headers, String apiKey) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
    }
}
