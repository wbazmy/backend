package com.wbazmy.backend.model.dto;

import com.wbazmy.backend.constant.enums.BuildModeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/3/3 - 11:14
 */
@Data
public class CronJobDto {
    private String jobName;
    private String jobType;
    private Long userId;
    private String cronExpression;
    private String projectName;
    private BuildModeEnum buildMode;
    private Date createTime;
    private Date lastExecutionTime;

}
