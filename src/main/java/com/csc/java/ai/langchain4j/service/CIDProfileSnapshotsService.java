package com.csc.java.ai.langchain4j.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.entity.CIDProfileSnapshots;
import com.csc.java.ai.langchain4j.entity.TrainingHistoryResource;

import java.util.List;

public interface CIDProfileSnapshotsService extends IService<CIDProfileSnapshots> {

    public CIDProfileSnapshots getOne(CIDProfileSnapshots cidProfileSnapshots);

    public List<TraineeProfileDTO> getTraineeProfileData(String collegeId);
}