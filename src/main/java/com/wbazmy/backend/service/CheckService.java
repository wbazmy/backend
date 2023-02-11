package com.wbazmy.backend.service;

import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.model.request.CheckRequest;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/10 - 15:17
 */
public interface CheckService {
    History depErrorCheck(CheckRequest request);
}
