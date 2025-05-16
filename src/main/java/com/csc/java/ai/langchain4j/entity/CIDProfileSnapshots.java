package com.csc.java.ai.langchain4j.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data // Lombok 注解自动生成 getter/setter/toString
@TableName("cid_profile_snapshots") // 表名映射
public class CIDProfileSnapshots {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("college_id")
    private String collegeId;

    @TableField("hkid_hash")
    private String hkidHash;

    @TableField("name")
    private String name;

    @TableField("name_pre")
    private String namePre;

    @TableField("org_id")
    private Integer orgId;

    @TableField("substantive_rank_id")
    private Integer substantiveRankId;

    @TableField("rank_id")
    private Integer rankId;

    @TableField("acting_rank_id")
    private Integer actingRankId;

    @TableField("first_pay_scale_id")
    private Integer firstPayScaleId;

    @TableField("first_pay_point_id")
    private Integer firstPayPointId;

    @TableField("current_pay_scale_id")
    private Integer currentPayScaleId;

    @TableField("current_pay_point_id")
    private Integer currentPayPointId;

    @TableField("employment_type")
    private String employmentType;

    /**
     * 日期字段映射
     * - PostgreSQL DATE → Java LocalDate
     */
    @TableField("appointment_date")
    private LocalDate appointmentDate;

    @TableField("last_duty_date")
    private LocalDate lastDutyDate;

    @TableField("last_service_date")
    private LocalDate lastServiceDate;

    @TableField("substantive_date")
    private LocalDate substantiveDate;

    @TableField("full_name")
    private String fullName;

    /**
     * 布尔字段映射
     * - PostgreSQL BOOL → Java Boolean
     */
    @TableField("is_older_50")
    private Boolean isOlder50;

    @TableField("is_related_icac")
    private Boolean isRelatedIcac;

    @TableField("reason")
    private String reason;

    @TableField("create_type")
    private String createType;

    @TableField("status")
    private String status;

    /**
     * 时间戳字段
     * - PostgreSQL TIMESTAMP → Java LocalDateTime
     */
    @TableField("last_sync_date")
    private LocalDateTime lastSyncDate;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("created_by")
    private Integer createdBy;

    @TableField("updated_by")
    private Integer updatedBy;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableField("deleted_by")
    private Integer deletedBy;

    @TableField("deleted_time")
    private LocalDateTime deletedTime;

    @TableField("api_process_task_id")
    private Long apiProcessTaskId;

    @TableField("current_department_name")
    private String currentDepartmentName;

    @TableField("first_rank_id")
    private Integer firstRankId;

    @TableField("previous_appointment_date")
    private LocalDate previousAppointmentDate;

    @TableField("previous_service_end_date")
    private LocalDate previousServiceEndDate;

    @TableField("previous_termination_date")
    private LocalDate previousTerminationDate;
}
