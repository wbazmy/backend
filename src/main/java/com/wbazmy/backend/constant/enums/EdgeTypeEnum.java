package com.wbazmy.backend.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/26 - 15:06
 */
@Getter
@AllArgsConstructor
public enum EdgeTypeEnum {
    NORMAL(1, "普通边"), MISSING(2, "缺失依赖"), REDUNDANT(3, "冗余依赖");
    private final Integer edgeType;
    private final String edgeMessage;
}
