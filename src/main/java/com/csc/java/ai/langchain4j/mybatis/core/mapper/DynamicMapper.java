package com.csc.java.ai.langchain4j.mybatis.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DynamicMapper {
    @Select("${sqlQuery}")
    List<Map<String, Object>> executeDynamicQuery(@Param("sqlQuery") String sqlQuery);
}
