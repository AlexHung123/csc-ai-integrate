package com.csc.java.ai.langchain4j.mybatis.core.mapper;

import com.csc.java.ai.langchain4j.domain.ChatSession;
import com.csc.java.ai.langchain4j.domain.vo.ChatSessionVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatSessionMapper extends BaseMapperPlus<ChatSession, ChatSessionVo>{
}
