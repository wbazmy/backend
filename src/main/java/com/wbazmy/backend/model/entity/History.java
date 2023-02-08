package com.wbazmy.backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wbazmy.backend.constant.enums.BuildModeEnum;
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
    @TableField(value = "detect_time", fill = FieldFill.INSERT)
    private Date detectTime;
    private Integer duration;
    private BuildModeEnum buildMode;
    private String lastCommitId;
    private String curCommitId;

}
