package com.wbazmy.backend.service;

import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.entity.History;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 17:01
 */
public interface HistoryService {
    void saveHistory(History history);

    PageInfo<History> pageHistory(String projectName, Integer pageNum, Integer pageSize);

    Boolean deleteHistory(Long id);

    History getHistory(Long id);
}
