package com.csc.java.ai.langchain4j.mybatis.core.mapper;

import com.csc.java.ai.langchain4j.domain.ChatModel;
import com.csc.java.ai.langchain4j.domain.vo.ChatModelVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatModelMapper extends BaseMapperPlus<ChatModel, ChatModelVo> {
}
