package com.wbazmy.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wbazmy.backend.constant.enums.RuleModeEnum;
import lombok.Data;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 11:10
 */
@Data
@TableName("rule")
public class Rule extends PublicField {
    private String ruleName;
    private RuleModeEnum ruleMode;
    private Long projectId;
    private String ruleContent;
    private Integer status;
    private String description;

}
