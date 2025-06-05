package com.csc.java.ai.langchain4j.controller;


import com.csc.java.ai.langchain4j.bean.BlockResponse;
import com.csc.java.ai.langchain4j.bean.StreamResponse;
import com.csc.java.ai.langchain4j.service.DifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/dify")
@RequiredArgsConstructor
public class DifyController {

    @Value("${dify.api.key}")
    private String testKey;

    private final DifyService difyService;

    @GetMapping("/block")
    public String test1() {
        String query = "who are you no_think";
        BlockResponse blockResponse = difyService.blockingMessage(query, 0L, testKey);
        return blockResponse.getAnswer();
    }

    @GetMapping("/stream")
    public Flux<StreamResponse> test2() {
        String query = "who are you no_think";
        return difyService.streamingMessage(query, 0L, testKey);
    }
}
