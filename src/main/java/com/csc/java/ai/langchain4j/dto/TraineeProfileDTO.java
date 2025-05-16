package com.csc.java.ai.langchain4j.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TraineeProfileDTO {
    private String collegeId;
    private LocalDateTime minCreatedTime;
    private String rankNameEn;
    private String departmentNameEn;
}