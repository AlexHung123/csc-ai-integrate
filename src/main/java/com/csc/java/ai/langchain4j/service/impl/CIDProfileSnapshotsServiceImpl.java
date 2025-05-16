package com.csc.java.ai.langchain4j.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.entity.CIDProfileSnapshots;
import com.csc.java.ai.langchain4j.mapper.CIDProfileSnapshotsMapper;
import com.csc.java.ai.langchain4j.service.CIDProfileSnapshotsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CIDProfileSnapshotsServiceImpl extends ServiceImpl<CIDProfileSnapshotsMapper, CIDProfileSnapshots> implements CIDProfileSnapshotsService {
    @Override
    public CIDProfileSnapshots getOne(CIDProfileSnapshots cidProfileSnapshots) {
        LambdaQueryWrapper<CIDProfileSnapshots> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CIDProfileSnapshots::getId, cidProfileSnapshots.getId());

        return baseMapper.selectOne(queryWrapper);
    }

    public List<TraineeProfileDTO> getTraineeProfileData(String collegeId) {
        return baseMapper.selectTraineeProfileData(collegeId);
    }
}
