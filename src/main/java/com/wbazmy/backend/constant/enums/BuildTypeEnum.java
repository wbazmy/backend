package com.wbazmy.backend.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/3 - 22:31
 */
@Getter
@AllArgsConstructor
public enum BuildTypeEnum {
    MAKE(1, "Make"), CMAKE(2, "Cmake"),
    AUTOTOOLS(3, "Autotools"), OTHER(4, "Other");
    @EnumValue
    private final Integer buildType;
    private final String buildTypeName;
}
