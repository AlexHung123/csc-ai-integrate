package com.csc.java.ai.langchain4j.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTools {

    @Tool(name = "加法運算")
    double sum(@ToolMemoryId int memoryId, @P(value = "加數1", required = true) double a, @P(value = "加數2", required = true) double b) {
        System.out.println("Function call plus memoryId " +  memoryId);
        return a + b;
    }

    @Tool(name = "平方根運算")
    double squareRoot(@ToolMemoryId int memoryId, double x) {
        System.out.println("Function call square memoryId " + memoryId);
        return Math.sqrt(x);
    }
}
