package com.wbazmy.backend.service;

import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.entity.Rule;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 17:02
 */
public interface RuleService {

    Rule createRule(Rule rule);

    Boolean deleteRule(Long ruleId);

    Rule updateRule(Rule rule);

    Rule getRule(Long ruleId);

    PageInfo<Rule> pageRule(String ruleName, Long projectId, Integer pageNum, Integer pageSize);
}
