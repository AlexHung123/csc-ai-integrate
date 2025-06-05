package com.csc.java.ai.langchain4j.controller;

import com.csc.java.ai.langchain4j.assistant.ITMSChatBIAgent;
import com.csc.java.ai.langchain4j.bean.ChatForm;
import com.csc.java.ai.langchain4j.mapper.DynamicMapper;
import com.csc.java.ai.langchain4j.service.RagflowService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "ITMS - Chat BI")
@RestController
@RequestMapping("/ai/itms/chat-bi")
public class ITMSChatBIController {

    private static final String AGENT_ID_AI_HEAD = "b7482380355311f0bc530242ac130006";
    private static final String AGENT_ID_TRAINING_GUIDE = "a41df9fc3f6111f0a7fb0242ac130006";
    private static final String DEFAULT_ERROR_MESSAGE = "Hello! I am an ITMS Chat BI System. If your question is about introducing myself, feel free to ask. If it's unrelated, please re-consider your question. I will not provide any other answers.";

    private final ITMSChatBIAgent itmsChatBIAgent;
    private final DynamicMapper dynamicMapper;
    private final RagflowService ragflowService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ITMSChatBIController(ITMSChatBIAgent itmsChatBIAgent, 
                               DynamicMapper dynamicMapper, 
                               RagflowService ragflowService) {
        this.itmsChatBIAgent = itmsChatBIAgent;
        this.dynamicMapper = dynamicMapper;
        this.ragflowService = ragflowService;
        this.objectMapper = new ObjectMapper();
    }

    @Operation(summary = "Stream chat response")
    @PostMapping(value = "/chat/stream", produces = "text/stream;charset=utf-8")
    public Flux<String> chatBIStream(@RequestBody ChatForm chatForm) {
        try {
            String sqlQuery = ragflowService.getStreamData(chatForm.getMessage() + " no_think", AGENT_ID_AI_HEAD);
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            if (result.isEmpty()) {
                return Flux.just("No records found in the system");
            }

            String resultString = objectMapper.writeValueAsString(result);
            return generateHtmlTableStream(resultString);
        } catch (Exception e) {
            System.err.println("Error during chat processing: " + e.getMessage());
            return Flux.just(DEFAULT_ERROR_MESSAGE);
        }
    }

    @Operation(summary = "Blocking chat response")
    @PostMapping(value = "/chat/block")
    public String chatBIBlock(@RequestBody ChatForm chatForm) {
        try {
            String sqlQuery = ragflowService.getStreamData(chatForm.getMessage(), AGENT_ID_AI_HEAD);
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            if (result.isEmpty()) {
                return "No records found in the system";
            }

            String resultString = objectMapper.writeValueAsString(result);
            return itmsChatBIAgent.chatByBlock(10L, chatForm.getMessage(), resultString);
        } catch (Exception e) {
            System.err.println("Error during chat processing: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    private static Flux<String> generateHtmlTableStream(String json) {
        return Flux.<String>create(emitter -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(json);

                emitter.next("<div style='margin-bottom: 16px; font-weight: 500;'>"
                        + "根據系統搜索，以下是返回的信息:"
                        + "</div>");

                emitter.next("<table border='1' style='none !important;'>\n  <thead>\n    <tr>");

                TableData tableData = extractTableData(rootNode);
                
                // Emit headers
                tableData.headers.stream()
                        .map(header -> "<th>" + escapeHtml(formatHeader(header)) + "</th>")
                        .forEach(emitter::next);
                emitter.next("</tr>\n  </thead>\n  <tbody>");

                // Emit rows
                tableData.rows.forEach(row -> {
                    StringBuilder sb = new StringBuilder("\n    <tr>");
                    tableData.headers.forEach(header ->
                            sb.append("<td>")
                                    .append(escapeHtml(row.getOrDefault(header, "")))
                                    .append("</td>")
                    );
                    sb.append("</tr>");
                    emitter.next(sb.toString());
                });

                emitter.next("\n  </tbody>\n</table>");
                emitter.complete();

            } catch (Exception e) {
                emitter.error(e);
            }
        }, FluxSink.OverflowStrategy.BUFFER)
        .onErrorResume(e ->
                Flux.just("<div style='color:red'>Error: " + escapeHtml(e.getMessage()) + "</div>")
        );
    }

    private static class TableData {
        final LinkedHashSet<String> headers;
        final List<Map<String, String>> rows;

        TableData(LinkedHashSet<String> headers, List<Map<String, String>> rows) {
            this.headers = headers;
            this.rows = rows;
        }
    }

    private static TableData extractTableData(JsonNode rootNode) {
        LinkedHashSet<String> headers = new LinkedHashSet<>();
        List<Map<String, String>> rows = new ArrayList<>();

        for (JsonNode node : rootNode) {
            Map<String, String> row = new LinkedHashMap<>();
            node.fieldNames().forEachRemaining(field -> {
                headers.add(field);
                row.put(field, node.get(field).asText());
            });
            rows.add(row);
        }

        return new TableData(headers, rows);
    }

    private static String formatHeader(String header) {
        return header.replace("_", " ")
                .replaceAll("(?<=[a-z])(?=[A-Z])", " ")
                .toLowerCase()
                .replaceFirst("^.", String.valueOf(Character.toUpperCase(header.charAt(0))));
    }

    private static String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
