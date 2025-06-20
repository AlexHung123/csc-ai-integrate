package com.csc.java.ai.langchain4j.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csc.java.ai.langchain4j.core.utils.MapstructUtils;
import com.csc.java.ai.langchain4j.core.utils.StringUtils;
import com.csc.java.ai.langchain4j.domain.ChatSession;
import com.csc.java.ai.langchain4j.domain.bo.ChatSessionBo;
import com.csc.java.ai.langchain4j.domain.vo.ChatSessionVo;
import com.csc.java.ai.langchain4j.mybatis.core.mapper.ChatSessionMapper;
import com.csc.java.ai.langchain4j.mybatis.core.page.PageQuery;
import com.csc.java.ai.langchain4j.mybatis.core.page.TableDataInfo;
import com.csc.java.ai.langchain4j.service.IChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ChatSessionServiceImpl implements IChatSessionService {
    private final ChatSessionMapper baseMapper;

    /**
     * 查询会话管理
     */
    @Override
    public ChatSessionVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询会话管理列表
     */
    @Override
    public TableDataInfo<ChatSessionVo> queryPageList(ChatSessionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ChatSession> lqw = buildQueryWrapper(bo);
        Page<ChatSessionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询会话管理列表
     */
    @Override
    public List<ChatSessionVo> queryList(ChatSessionBo bo) {
        LambdaQueryWrapper<ChatSession> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ChatSession> buildQueryWrapper(ChatSessionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ChatSession> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, ChatSession::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getSessionTitle()), ChatSession::getSessionTitle, bo.getSessionTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getSessionContent()), ChatSession::getSessionContent, bo.getSessionContent());
        return lqw;
    }

    /**
     * 新增会话管理
     */
    @Override
    public Boolean insertByBo(ChatSessionBo bo) {
        ChatSession add = MapstructUtils.convert(bo, ChatSession.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改会话管理
     */
    @Override
    public Boolean updateByBo(ChatSessionBo bo) {
        ChatSession update = MapstructUtils.convert(bo, ChatSession.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ChatSession entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除会话管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
