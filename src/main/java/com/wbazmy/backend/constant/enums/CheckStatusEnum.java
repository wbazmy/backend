package com.wbazmy.backend.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/10 - 15:40
 */
@Getter
@AllArgsConstructor
public enum CheckStatusEnum {
    FINISHED(1, "已完成"), FAILED(2, "失败"), CHECKING(3, "进行中");
    @EnumValue
    private final Integer checkStatus;
    private final String checkMessage;
}
