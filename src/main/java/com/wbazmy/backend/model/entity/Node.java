package com.wbazmy.backend.model.entity;

import com.wbazmy.backend.constant.enums.NodeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/26 - 21:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    private String name;
    private NodeTypeEnum nodeType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
