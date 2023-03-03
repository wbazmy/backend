package com.wbazmy.backend.model.request;

import com.wbazmy.backend.constant.enums.BuildModeEnum;
import lombok.Data;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/3/3 - 11:32
 */
@Data
public class CronJobRequest {
    private String jobName;
    private String jobType;
    private String cronExpression;
    private String projectName;
    private Long projectId;
    private BuildModeEnum buildMode;

}
