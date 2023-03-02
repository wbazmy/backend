package com.wbazmy.backend.model.entity;

import com.wbazmy.backend.constant.enums.EdgeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/26 - 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Edge {
    private String source;
    private String target;
    private EdgeTypeEnum edgeType;

}
