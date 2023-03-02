package com.wbazmy.backend.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/26 - 21:56
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum {
    INPUT(1, "输入节点"), OUTPUT(2, "输出节点");
    private final Integer nodeType;
    private final String nodeMessage;
}
