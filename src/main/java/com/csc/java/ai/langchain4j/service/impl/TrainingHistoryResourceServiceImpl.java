package com.csc.java.ai.langchain4j.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csc.java.ai.langchain4j.dto.TrainingHistoryDTO;
import com.csc.java.ai.langchain4j.entity.TrainingHistoryResource;
import com.csc.java.ai.langchain4j.mybatis.core.mapper.TrainingHistoryResourceMapper;
import com.csc.java.ai.langchain4j.service.TrainingHistoryResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingHistoryResourceServiceImpl extends ServiceImpl<TrainingHistoryResourceMapper, TrainingHistoryResource> implements TrainingHistoryResourceService {
    @Override
    public TrainingHistoryResource getOne(TrainingHistoryResource trainingHistoryResource) {

        LambdaQueryWrapper<TrainingHistoryResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainingHistoryResource::getId, trainingHistoryResource.getId());
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<TrainingHistoryDTO> selectTrainingHistoryDataByCollegeId(String collegeId) {
        return baseMapper.selectTrainingHistoryDataByCollegeId(collegeId);
    }
}
