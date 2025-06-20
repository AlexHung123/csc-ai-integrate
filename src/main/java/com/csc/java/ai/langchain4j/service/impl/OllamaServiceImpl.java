package com.csc.java.ai.langchain4j.service.impl;

import com.csc.java.ai.langchain4j.core.enums.ChatModeType;
import com.csc.java.ai.langchain4j.core.utils.SSEUtil;
import com.csc.java.ai.langchain4j.domain.vo.ChatModelVo;
import com.csc.java.ai.langchain4j.entity.chat.Message;
import com.csc.java.ai.langchain4j.request.ChatRequest;
import com.csc.java.ai.langchain4j.service.IChatModelService;
import com.csc.java.ai.langchain4j.service.IChatService;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.chat.OllamaChatMessage;
import io.github.ollama4j.models.chat.OllamaChatMessageRole;
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder;
import io.github.ollama4j.models.chat.OllamaChatRequestModel;
import io.github.ollama4j.models.generate.OllamaStreamHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OllamaServiceImpl implements IChatService {

    @Autowired
    private IChatModelService chatModelService;

    @Override
    public SseEmitter chat(ChatRequest chatRequest, SseEmitter emitter) {

        ChatModelVo chatModelVo = chatModelService.selectModelByName(chatRequest.getModel());
        String apiHost = chatModelVo.getApiHost();
        List<Message> msgList = chatRequest.getMessages();

        List<OllamaChatMessage> messages = new ArrayList<>();
        for (Message message : msgList) {
            OllamaChatMessage ollamaChatMessage = new OllamaChatMessage();
            ollamaChatMessage.setRole(OllamaChatMessageRole.USER);
            ollamaChatMessage.setContent(message.getContent().toString());
            messages.add(ollamaChatMessage);
        }

        OllamaAPI api = new OllamaAPI(apiHost);
        api.setRequestTimeoutSeconds(100);
        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(chatRequest.getModel());

        OllamaChatRequestModel requestModel = builder
                .withMessages(messages)
                .build();

        CompletableFuture.runAsync(() -> {
            try {
                StringBuilder response = new StringBuilder();
                OllamaStreamHandler streamHandler = (s) -> {
                    String substr = s.substring(response.length());
                    response.append(substr);
                    try {
                        emitter.send(substr);
                    } catch (IOException e) {
                        SSEUtil.sendErrorEvent(emitter, e.getMessage());
                    }
                };
                api.chat(requestModel, streamHandler);
                emitter.complete();
            } catch (Exception e) {
                SSEUtil.sendErrorEvent(emitter, e.getMessage());
            }
        });

        return emitter;

    }

    @Override
    public String getCategory() {
        return ChatModeType.OLLAMA.getCode();
    }
}
