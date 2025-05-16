package com.csc.java.ai.langchain4j.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.dto.TrainingHistoryDTO;
import com.csc.java.ai.langchain4j.entity.TrainingHistoryResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrainingHistoryResourceService extends IService<TrainingHistoryResource> {

    TrainingHistoryResource getOne(TrainingHistoryResource trainingHistoryResource);
    List<TrainingHistoryDTO> selectTrainingHistoryDataByCollegeId(String collegeId);
}