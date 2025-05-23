package com.csc.java.ai.langchain4j.controller;

import com.csc.java.ai.langchain4j.assistant.ITMSAgent;
import com.csc.java.ai.langchain4j.assistant.ITMSChatBIAgent;
import com.csc.java.ai.langchain4j.bean.ChatForm;
import com.csc.java.ai.langchain4j.mapper.DynamicMapper;
import com.csc.java.ai.langchain4j.service.RagflowService;
import com.csc.java.ai.langchain4j.tools.GenerateTraineeProfileTools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping(value = "/chat")
    public String chatBI(@RequestBody ChatForm chatForm) {
        try {
            System.out.println("--------------------");
            System.out.println(chatForm.getMessage());
            String sqlQuery = ragflowService.getStreamData(chatForm.getMessage());
            System.out.println(sqlQuery);
            List<Map<String, Object>> result = dynamicMapper.executeDynamicQuery(sqlQuery);

            if (result.isEmpty()) {
                return "[]";
            }

            String resultString = objectMapper.writeValueAsString(result);
            System.out.println(resultString);
            return generateHtmlTable(resultString);

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
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
