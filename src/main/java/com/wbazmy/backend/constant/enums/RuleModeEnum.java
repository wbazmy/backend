package com.wbazmy.backend.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 12:42
 */
@Getter
@AllArgsConstructor
public enum RuleModeEnum {
    EXCLUDE_TARGET(1, "exclude_target"), EXCLUDE_PATH(2, "exclude_path"),
    EXCLUDE_SUFFIX(3, "exclude_suffix");
    @EnumValue
    private final Integer ruleMode;
    private final String ruleModeName;

}
