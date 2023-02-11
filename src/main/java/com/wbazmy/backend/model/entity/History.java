package com.wbazmy.backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wbazmy.backend.constant.enums.BuildModeEnum;
import com.wbazmy.backend.constant.enums.CheckStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 11:10
 */
@Data
@TableName("history")
public class History {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private Integer mdNum;
    private Integer rdNum;
    private Date startTime;
    private Date endTime;
    private Integer duration;
    private BuildModeEnum buildMode;
    private CheckStatusEnum checkStatus;
    private String baseCommitId;
    private String curCommitId;

}
