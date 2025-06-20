package com.csc.java.ai.langchain4j.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csc.java.ai.langchain4j.dto.TrainingHistoryDTO;
import com.csc.java.ai.langchain4j.entity.TrainingHistoryResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TrainingHistoryResourceMapper extends BaseMapper<TrainingHistoryResource> {

    List<TrainingHistoryDTO> selectTrainingHistoryDataByCollegeId(@Param("collegeId") String collegeId);
}
