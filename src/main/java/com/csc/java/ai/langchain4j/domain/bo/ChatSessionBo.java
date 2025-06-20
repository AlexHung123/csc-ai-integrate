package com.csc.java.ai.langchain4j.domain.bo;

import com.csc.java.ai.langchain4j.domain.ChatSession;
import com.csc.java.ai.langchain4j.entity.BaseEntity;
import com.csc.java.ai.langchain4j.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 会话管理业务对象 chat_session
 *
 * @author ageerle
 * @date 2025-05-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ChatSession.class, reverseConvertGenerate = false)
public class ChatSessionBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 会话标题
     */
    private String sessionTitle;

    /**
     * 会话内容
     */
    private String sessionContent;

    /**
     * 备注
     */
    private String remark;


}
