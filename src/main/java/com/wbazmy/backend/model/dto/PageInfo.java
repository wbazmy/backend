package com.wbazmy.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/7 - 19:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo<T> {
    private Integer currentPage;
    private Integer pageSize;
    private Integer pageTotalNum;
    private Integer recordTotalNum;
    private List<T> data;
}
