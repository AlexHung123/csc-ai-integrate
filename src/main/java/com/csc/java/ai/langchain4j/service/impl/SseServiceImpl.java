package com.csc.java.ai.langchain4j.service.impl;

import com.csc.java.ai.langchain4j.core.utils.DateUtils;
import com.csc.java.ai.langchain4j.core.utils.StringUtils;
import com.csc.java.ai.langchain4j.domain.vo.ChatModelVo;
import com.csc.java.ai.langchain4j.entity.chat.BaseMessage;
import com.csc.java.ai.langchain4j.entity.chat.Message;
import com.csc.java.ai.langchain4j.factory.ChatServiceFactory;
import com.csc.java.ai.langchain4j.request.ChatRequest;
import com.csc.java.ai.langchain4j.service.IChatModelService;
import com.csc.java.ai.langchain4j.service.IChatSessionService;
import com.csc.java.ai.langchain4j.service.ISseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseServiceImpl implements ISseService {

    private final IChatModelService chatModelService;

    private final ChatServiceFactory chatServiceFactory;

    private final IChatSessionService chatSessionService;

    private ChatModelVo chatModelVo;

    @Override
    public SseEmitter sseChat(ChatRequest chatRequest, HttpServletRequest request) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        try {

        }
    }


    private void buildChatMessageList(ChatRequest chatRequest) {
        String sysPrompt;

        if(chatRequest.getModel().equals("gpt-image")) {
            chatModelVo = chatModelService.selectModelByCategory("image");
            if(chatModelVo == null) {
                log.error("Cannot found model configuration for image type");
                throw new IllegalStateException("Cannot found model configuration for image type");
            }
        }else {
            chatModelVo = chatModelService.selectModelByName(chatRequest.getModel());
        }

        List<Message> messages = chatRequest.getMessages();

        sysPrompt = chatModelVo.getSystemPrompt();
        if(StringUtils.isEmpty(sysPrompt)) {
            sysPrompt = "You are an AI assistant developed by iTMS, named iTMS Assistant. You are good at Chinese and English conversations, can understand and handle various issues, and provide safe, helpful and accurate answers." +
                    "Current time: "+ DateUtils.getDate()+
                    "#Note: Before replying, pay attention to the context and the tool return content.";
        }

        Message.builder().content(sysPrompt).role(Message)
    }
}
