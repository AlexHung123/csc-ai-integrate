package com.csc.java.ai.langchain4j.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("training_history_resource")
public class TrainingHistoryResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("college_id")
    private String collegeId;

    @TableField("display_name")
    private String displayName;

    @TableField("first_name")
    private String firstName;

    @TableField("last_name")
    private String lastName;

    @TableField("chinese_name")
    private String chineseName;

    @TableField("resource_code")
    private String resourceCode;

    @TableField("resource_type")
    private String resourceType;

    @TableField("resource_name_en")
    private String resourceNameEn;

    @TableField("resource_name_tc")
    private String resourceNameTc;

    @TableField("resource_name_sc")
    private String resourceNameSc;

    @TableField("resource_topic_id")
    private Long resourceTopicId;

    @TableField("source_application_id")
    private String sourceApplicationId;

    @TableField("complete_date")
    private LocalDateTime completeDate;

    @TableField("completion_status")
    private String completionStatus;

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField("created_by")
    private Integer createdBy;

    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableField("updated_by")
    private Integer updatedBy;

    @TableField("deleted_by")
    private Integer deletedBy;

    @TableField("deleted_time")
    private LocalDateTime deletedTime;

    @TableField("external_resource_type")
    private String externalResourceType;

    @TableField("org_code")
    private String orgCode;

    @TableField("rank_code")
    private String rankCode;

    @TableField("org_desc")
    private String orgDesc;

    @TableField("rank_desc")
    private String rankDesc;

    @TableField("wcms_resource_type")
    private String wcmsResourceType;

    @TableField("learning_credit")
    private Short learningCredit;

    @TableField("resource_id")
    private Long resourceId;

    @TableField("link_en")
    private String linkEn;

    @TableField("link_tc")
    private String linkTc;

    @TableField("link_sc")
    private String linkSc;

    @TableField("is_restudying")
    private Boolean isRestudying;

    @TableField("elc_resource_type")
    private String elcResourceType;
}
