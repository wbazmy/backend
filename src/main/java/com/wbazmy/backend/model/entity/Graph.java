package com.wbazmy.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/26 - 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Graph {
    private Map<String, Node> nodes = new HashMap<>();
    private List<Edge> edges = new ArrayList<>();

}
