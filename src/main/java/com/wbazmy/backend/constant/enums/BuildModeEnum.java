package com.wbazmy.backend.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 11:12
 */
@Getter
@AllArgsConstructor
public enum BuildModeEnum {
    CLEAN_BUILD(1, "全量构建"), INCREMENTAL_BUILD(2, "增量构建");
    @EnumValue
    private final Integer buildMode;
    private final String buildModeName;
}
