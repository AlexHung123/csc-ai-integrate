package com.csc.java.ai.langchain4j.mybatis.core.mapper;

import com.csc.java.ai.langchain4j.domain.ChatMessage;
import com.csc.java.ai.langchain4j.domain.vo.ChatMessageVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMessageMapper extends BaseMapperPlus<ChatMessage, ChatMessageVo> {
}
