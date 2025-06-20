package com.csc.java.ai.langchain4j.factory;

import com.csc.java.ai.langchain4j.service.IChatModelService;
import com.csc.java.ai.langchain4j.service.IChatService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatServiceFactory implements ApplicationContextAware {

    private final Map<String, IChatService> chatServiceMap = new ConcurrentHashMap<>();
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IChatService> serviceMap = applicationContext.getBeansOfType(IChatService.class);
        for (IChatService service : serviceMap.values()) {
            if (service != null) {
                chatServiceMap.put(service.getCategory(), service);
            }
        }
    }


    public IChatService getChatService (String category) {
        IChatService service = chatServiceMap.get(category);
        if (service == null) {
            throw new IllegalArgumentException("Not support model type: " + category);
        }

        return service;
    }
}
