package com.csc.java.ai.langchain4j.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csc.java.ai.langchain4j.core.utils.MapstructUtils;
import com.csc.java.ai.langchain4j.core.utils.StringUtils;
import com.csc.java.ai.langchain4j.domain.ChatModel;
import com.csc.java.ai.langchain4j.domain.bo.ChatModelBo;
import com.csc.java.ai.langchain4j.domain.vo.ChatModelVo;
import com.csc.java.ai.langchain4j.mybatis.core.mapper.ChatModelMapper;
import com.csc.java.ai.langchain4j.mybatis.core.page.PageQuery;
import com.csc.java.ai.langchain4j.mybatis.core.page.TableDataInfo;
import com.csc.java.ai.langchain4j.service.IChatModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ChatModelServiceImpl implements IChatModelService {

    private final ChatModelMapper baseMapper;

    @Override
    public ChatModelVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<ChatModelVo> queryPageList(ChatModelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ChatModel> lqw = buildQueryWrapper(bo);
        Page<ChatModelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<ChatModelVo> queryList(ChatModelBo bo) {
        LambdaQueryWrapper<ChatModel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ChatModel> buildQueryWrapper(ChatModelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ChatModel> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), ChatModel::getCategory, bo.getCategory());
        lqw.like(StringUtils.isNotBlank(bo.getModelName()), ChatModel::getModelName, bo.getModelName());
        lqw.eq(StringUtils.isNotBlank(bo.getModelDescribe()), ChatModel::getModelDescribe, bo.getModelDescribe());
        lqw.eq(bo.getModelPrice() != null, ChatModel::getModelPrice, bo.getModelPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getModelType()), ChatModel::getModelType, bo.getModelType());
        lqw.eq(StringUtils.isNotBlank(bo.getModelShow()), ChatModel::getModelShow, bo.getModelShow());
        lqw.eq(StringUtils.isNotBlank(bo.getSystemPrompt()), ChatModel::getSystemPrompt, bo.getSystemPrompt());
        lqw.eq(StringUtils.isNotBlank(bo.getApiHost()), ChatModel::getApiHost, bo.getApiHost());
        lqw.eq(StringUtils.isNotBlank(bo.getApiKey()), ChatModel::getApiKey, bo.getApiKey());
        return lqw;
    }

    @Override
    public Boolean insertByBo(ChatModelBo bo) {
        ChatModel add = MapstructUtils.convert(bo, ChatModel.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(ChatModelBo bo) {
        return null;
    }

    private void validEntityBeforeSave(ChatModel entity){
        // 判断是否包含*号
        if (entity.getApiKey().contains("*")) {
            // 重新设置key信息
            entity.setApiKey(baseMapper.selectById(entity.getId()).getApiKey());
        }
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){

        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public ChatModelVo selectModelByName(String modelName) {
        return baseMapper.selectVoOne(Wrappers.<ChatModel>lambdaQuery().eq(ChatModel::getModelName, modelName));
    }

    @Override
    public ChatModelVo selectModelByCategory(String category) {
        return baseMapper.selectVoOne(Wrappers.<ChatModel>lambdaQuery().eq(ChatModel::getCategory, category));
    }
}
