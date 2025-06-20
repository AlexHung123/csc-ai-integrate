package com.csc.java.ai.langchain4j.service.impl;

import com.csc.java.ai.langchain4j.core.enums.ChatModeType;
import com.csc.java.ai.langchain4j.domain.vo.ChatModelVo;
import com.csc.java.ai.langchain4j.request.ChatRequest;
import com.csc.java.ai.langchain4j.service.IChatModelService;
import com.csc.java.ai.langchain4j.service.IChatService;
import io.github.imfangs.dify.client.DifyClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.callback.ChatStreamCallback;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.event.ErrorEvent;
import io.github.imfangs.dify.client.event.MessageEndEvent;
import io.github.imfangs.dify.client.event.MessageEvent;
import io.github.imfangs.dify.client.model.DifyConfig;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class DifyServiceImpl implements IChatService {

    @Autowired
    private IChatModelService chatModelService;

    @Override
    public SseEmitter chat(ChatRequest chatRequest, SseEmitter emitter) {

        ChatModelVo chatModelVo = chatModelService.selectModelByName(chatRequest.getModel());
        DifyConfig config = DifyConfig.builder()
                .baseUrl(chatModelVo.getApiHost())
                .apiKey(chatModelVo.getApiKey())
                .connectTimeout(5000)
                .readTimeout(60000)
                .writeTimeout(30000)
                .build();

        DifyClient difyClient = DifyClientFactory.createClient(config);

        ChatMessage message = ChatMessage.builder()
                .query(chatRequest.getPrompt())
                .user(chatRequest.getUserId().toString())
                .responseMode(ResponseMode.STREAMING)
                .build();

        try {
            difyClient.sendChatMessageStream(message, new ChatStreamCallback() {

                @SneakyThrows
                @Override
                public void onMessage(MessageEvent event) {
                    emitter.send(event.getAnswer());
                    log.info("Receive message fragment: {}", event.getAnswer());
                }

                @Override
                public void onMessageEnd(MessageEndEvent event) {
                    emitter.complete();
                    log.info("End of message, completed message ID: {}", event.getMessageId());
                }

                @Override
                public void onError(ErrorEvent event) {
                    ChatStreamCallback.super.onError(event);
                }

                @Override
                public void onException(Throwable throwable) {
                    System.err.println("Exception: " + throwable.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Dify request failed: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public String getCategory() {
        return ChatModeType.DIFY.getCode();
    }
}
