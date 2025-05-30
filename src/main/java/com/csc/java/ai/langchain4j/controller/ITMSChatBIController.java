package com.csc.java.ai.langchain4j.controller;

import com.csc.java.ai.langchain4j.assistant.ITMSChatBIAgent;
import com.csc.java.ai.langchain4j.bean.ChatForm;
import com.csc.java.ai.langchain4j.mapper.DynamicMapper;
import com.csc.java.ai.langchain4j.service.RagflowService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.*;

@Tag(name = "ITMS - Chat BI")
@RestController
@RequestMapping("/ai/itms/chat-bi")
public class ITMSChatBIController {

    @Autowired
    private ITMSChatBIAgent itmsChatBIAgent;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private RagflowService ragflowService;

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Operation(summary = "chat")
//    @PostMapping("/chat")
//    public String chat(@RequestBody ChatForm chatForm) {
//        return itmsAgent.chat(chatForm.getMemoryId(), generateTraineeProfileTools.generateTraineeProfileEnglishCoverDescription("CS943939"));
//    }


    @PostMapping(value = "/chat/stream", produces = "text/stream;charset=utf-8")
    public Flux<String> chatBIStream(@RequestBody ChatForm chatForm) {
        try {
            String sqlQuery = ragflowService.getStreamData(chatForm.getMessage() + " no_think");
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            System.out.println(result);

            if (result.isEmpty()) {
                return Flux.just("No any records found in the system");
            }

            String resultString = objectMapper.writeValueAsString(result);
            System.out.println(resultString);
            String userMessage = "用戶問題: " + chatForm.getMessage() + " api響應内容: " + resultString + " 請根據用戶問題和api響應内容，展示友好的格式給用戶,必須嚴格api響應内容去展示，不要做任何修改 " + " no_think";
            System.out.println(userMessage);
            return itmsChatBIAgent.chat(15L, userMessage, resultString);
//            return generateHtmlTableStream(resultString);
        } catch (Exception e) {
            System.err.println("Error during chat processing: " + e.getMessage());
            return itmsChatBIAgent.chatByResponseNull(10L, chatForm.getMessage());
//            return Flux.just("Error: " + e.getMessage());
        }
    }


    @PostMapping(value = "/chat/block")
    public String chatBIBlock(@RequestBody ChatForm chatForm) {
        try {
            String sqlQuery = ragflowService.getStreamData(chatForm.getMessage());
            System.out.println(sqlQuery);
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            if (result.isEmpty()) {
                return "";
            }

            String resultString = objectMapper.writeValueAsString(result);
            System.out.println(resultString);
            return itmsChatBIAgent.chatByBlock(10L, chatForm.getMessage(), resultString);
        } catch (Exception e) {
            System.err.println("Error during chat processing: " + e.getMessage());
            return e.getMessage();
        }
    }

    public static Flux<String> generateHtmlTableStream(String json) {
        return Flux.<String>create(emitter -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode rootNode = mapper.readTree(json);

                        // 新增: 系統提示文字(使用Ant Design樣式)
                        emitter.next("<div style='margin-bottom: 16px; font-weight: 500;'>"
                                + "根據系統搜索，以下是返回的信息:"
                                + "</div>");

                        // 階段1: 發送表格框架(增加Ant Design樣式)
//                        emitter.next("<table style='border-collapse: collapse; width: 100%;"
//                                + "border: 1px solid #e8e8e8; margin-bottom: 24px;'>\n"
//                                + "  <thead>\n    <tr style='background-color: #fafafa;'>");

                        // 階段1: 發送表格框架
                        emitter.next("<table border='1' style='none !important;'>\n  <thead>\n    <tr>");

//                        emitter.next("<table style='border-collapse: collapse; border-top: 1px solid #e8e8e8; border-bottom: 1px solid #e8e8e8; border-left: none; border-right: none;'>\n  <thead>\n    <tr>");
                        // 階段2: 動態收集表頭
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

                        // 階段3: 流式輸出表頭
                        headers.stream()
                                .map(header -> "<th>" + escapeHtml(formatHeader(header)) + "</th>")
                                .forEach(emitter::next);
                        emitter.next("</tr>\n  </thead>\n  <tbody>");

                        // 階段4: 流式輸出數據行
                        rows.stream()
                                .forEach(row -> {  // 直接使用forEach操作row對象
                                    StringBuilder sb = new StringBuilder("\n    <tr>");
                                    headers.forEach(header ->
                                            sb.append("<td>")
                                                    .append(escapeHtml(row.getOrDefault(header, "")))
                                                    .append("</td>")
                                    );
                                    sb.append("</tr>");
                                    emitter.next(sb.toString());
                                });

                        // 階段5: 發送表格閉合標籤
                        emitter.next("\n  </tbody>\n</table>");
                        emitter.complete();

                    } catch (Exception e) {
                        emitter.error(e); // 錯誤處理
                    }
                }, FluxSink.OverflowStrategy.BUFFER)
                .onErrorResume(e ->
                        Flux.just("<div style='color:red'>Error: " + escapeHtml(e.getMessage()) + "</div>")
                );
    }

    public static Flux<String> generateMarkdownTableStream(String json) {
        return Flux.<String>create(emitter -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode rootNode = mapper.readTree(json);

                        // 階段0: 發送Markdown提示文字
                        emitter.next("### 根據系統搜索，以下是返回的信息\n\n");

                        // 階段1: 動態收集表頭
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

                        // 階段2: 流式輸出表頭
                        StringBuilder headerLine = new StringBuilder("|");
                        StringBuilder separatorLine = new StringBuilder("|");
                        headers.forEach(header -> {
                            headerLine.append(escapeHtml(formatHeader(header))).append("|");
                            separatorLine.append("---|");
                        });

                        emitter.next(headerLine.toString());
                        emitter.next(separatorLine.toString());

                        // 階段3: 流式輸出數據行
                        rows.stream()
                                .map(row -> {
                                    StringBuilder rowLine = new StringBuilder("|");
                                    headers.forEach(header ->
                                            rowLine.append(escapeHtml(row.getOrDefault(header, ""))).append("|")
                                    );
                                    return rowLine.toString();
                                })
                                .forEach(emitter::next);

                        emitter.complete();

                    } catch (Exception e) {
                        emitter.error(e);
                    }
                }, FluxSink.OverflowStrategy.BUFFER)
                .onErrorResume(e ->
                        Flux.just("```\n[ERROR] " + escapeHtml(e.getMessage()) + "\n```")
                );
    }

    public static String generateHtmlTable(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);

        // 動態收集字段並保持順序[1,2](@ref)
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

        // 構建HTML結構[3,4](@ref)
        StringBuilder html = new StringBuilder()
                .append("<table border='1' style='border-collapse: collapse;'>\n")
                .append("  <thead>\n    <tr>");

        // 生成表頭[2](@ref)
        for (String header : headers) {
            String displayHeader = formatHeader(header);
            html.append("<th>")
                    .append(escapeHtml(displayHeader))
                    .append("</th>");
        }
        html.append("</tr>\n  </thead>\n  <tbody>");

        // 生成數據行[5](@ref)
        for (Map<String, String> row : rows) {
            html.append("\n    <tr>");
            for (String header : headers) {
                String value = row.getOrDefault(header, "");
                html.append("<td>")
                        .append(escapeHtml(value))
                        .append("</td>");
            }
            html.append("</tr>");
        }

        return html.append("\n  </tbody>\n</table>").toString();
    }

    // 新增的核心轉換方法
    private static String formatHeader(String header) {
        if (header == null || header.isEmpty()) return header;

        String[] parts = header.split("_");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                String formatted = part.substring(0, 1).toUpperCase() +
                        part.substring(1).toLowerCase();
                result.append(formatted).append(" ");
            }
        }
        return result.toString().trim();
    }

    // HTML特殊字符轉義[3](@ref)
    private static String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }


}
