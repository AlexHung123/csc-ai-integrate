package com.csc.java.ai.langchain4j.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csc.java.ai.langchain4j.dto.TraineeProfileDTO;
import com.csc.java.ai.langchain4j.entity.CIDProfileSnapshots;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CIDProfileSnapshotsMapper extends BaseMapper<CIDProfileSnapshots> {
    List<TraineeProfileDTO> selectTraineeProfileData(@Param("collegeId") String collegeId);

}
