package com.csc.java.ai.langchain4j.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingHistoryDTO {

    private String collegeId;
    private String name;
    private String courseName;
    private String createdTime;
}
