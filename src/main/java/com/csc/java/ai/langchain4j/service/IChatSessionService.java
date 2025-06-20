package com.csc.java.ai.langchain4j.service;

import com.csc.java.ai.langchain4j.domain.bo.ChatSessionBo;
import com.csc.java.ai.langchain4j.domain.vo.ChatSessionVo;
import com.csc.java.ai.langchain4j.mybatis.core.page.PageQuery;
import com.csc.java.ai.langchain4j.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

public interface IChatSessionService {
    /**
     * 查询会话管理
     */
    ChatSessionVo queryById(Long id);

    /**
     * 查询会话管理列表
     */
    TableDataInfo<ChatSessionVo> queryPageList(ChatSessionBo bo, PageQuery pageQuery);

    /**
     * 查询会话管理列表
     */
    List<ChatSessionVo> queryList(ChatSessionBo bo);

    /**
     * 新增会话管理
     */
    Boolean insertByBo(ChatSessionBo bo);

    /**
     * 修改会话管理
     */
    Boolean updateByBo(ChatSessionBo bo);

    /**
     * 校验并批量删除会话管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
