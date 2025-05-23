package com.csc.java.ai.langchain4j.service;

import com.alibaba.fastjson2.JSON;
import java.util.HashMap;
import java.util.List;

import com.csc.java.ai.langchain4j.bean.BlockResponse;
import com.csc.java.ai.langchain4j.bean.DifyRequestBody;
import com.csc.java.ai.langchain4j.bean.StreamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Service
@RequiredArgsConstructor
public class DifyService {

    @Value("${dify.url}")
    private String url;

    private final RestTemplate restTemplate;

    private final WebClient webClient;

    public Flux<StreamResponse> streamingMessage(String query, Long userId, String apiKey) {
        DifyRequestBody body = new DifyRequestBody();
        body.setInputs(new HashMap<>());
        body.setQuery(query);
        body.setResponseMode("streaming");
        body.setConversationId("");
        body.setUser(userId.toString());
        return webClient.post()
                .uri(url)
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    httpHeaders.setBearerAuth(apiKey);
                })
                .bodyValue(JSON.toJSONString(body))
                .retrieve()
                .bodyToFlux(StreamResponse.class);
    }


    public BlockResponse blockingMessage(String query, Long userId, String apiKey) {
        DifyRequestBody body = new DifyRequestBody();
        body.setInputs(new HashMap<>());
        body.setQuery(query);
        body.setResponseMode("blocking");
        body.setConversationId("");
        body.setUser(userId.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(apiKey);
        String jsonString = JSON.toJSONString(body);
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
        System.out.println("------------------");
        System.out.println(entity);
        ResponseEntity<BlockResponse> stringResponseEntity =
                restTemplate.postForEntity(url, entity, BlockResponse.class);
        return stringResponseEntity.getBody();
    }
}
