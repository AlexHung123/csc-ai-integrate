package com.csc.java.ai.langchain4j.assistant;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
//        chatModel = "ollamaChatModel",
//        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel",
//        streamingChatModel = "ollamaStreamingChatModel",
        chatMemoryProvider = "chatBIMemoryProviderITMS"
)
public interface ITMSChatBIAgent {
//    QWEN3
//    @SystemMessage("你是一個ITMS Chat BI System,你只需要介紹自己用戶重新審視問題，不要自己做任何其他回答 no_think")
    @UserMessage("Please response as below: Hello! I am an ITMS Chat BI System. If your question is about introducing myself, feel free to ask. If it's unrelated, please re-consider your question. I will not provide any other answers.")
    Flux<String> chatByResponseNull(@MemoryId Long memoryId, @V("message") String userMessage);
//    @SystemMessage("請針對用戶輸入的問題和api響應内容， 顯示友好的格式給用戶，必須嚴格api響應内容去展示，不要做任何修改")
//    @SystemMessage("請針對用戶輸入的問題和api響應内容， 顯示友好的格式給用戶，必須嚴格api響應内容去展示，不要做任何修改")
//    @UserMessage("用戶問題:{{message}} api響應内容: {{answer}}")
    @UserMessage("{{message}}")
    Flux<String> chat(@MemoryId Long memoryId, @V("message") String userMessage, @V("answer") String responseAnswer);

    @SystemMessage("請針對用戶輸入的問題和api響應内容， 轉化為容易解讀的信息給用戶，不需要額外添加其他元素 no_think")
    @UserMessage("no_think 問題:{{message}} api響應内容: {{answer}}")
    String chatByBlock(@MemoryId Long memoryId, @V("message") String userMessage, @V("answer") String responseAnswer);
}
