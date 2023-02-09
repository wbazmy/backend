package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.dao.RuleRepository;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.entity.Rule;
import com.wbazmy.backend.service.RuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 17:02
 */
@Service
public class RuleServiceImpl implements RuleService {

    @Resource
    private RuleRepository ruleRepository;

    @Override
    public Rule createRule(Rule rule) {
        Rule oldRule = ruleRepository.findByRuleNameAndProjectId(rule.getRuleName(), rule.getProjectId());
        if (oldRule != null) {
            return null;
        }
        ruleRepository.saveRule(rule);
        return ruleRepository.findByRuleNameAndProjectId(rule.getRuleName(), rule.getProjectId());
    }

    @Override
    public Boolean deleteRule(Long ruleId) {
        Rule rule = ruleRepository.findById(ruleId);
        if (rule == null) {
            return Boolean.FALSE;
        }
        ruleRepository.deleteRule(ruleId);
        return Boolean.TRUE;
    }

    @Override
    public Rule updateRule(Rule rule) {
        Rule oldRule = ruleRepository.findById(rule.getId());
        if (oldRule == null) {
            return null;
        }
        if (!Objects.isNull(rule.getRuleName()) && !Objects.equals(rule.getRuleName(), oldRule.getRuleName())) {
            Rule oldRule1 = ruleRepository.findByRuleNameAndProjectId(rule.getRuleName(), oldRule.getProjectId());
            if (oldRule1 != null) {
                return null;
            }
        }
        ruleRepository.updateRule(rule);
        return ruleRepository.findById(rule.getId());
    }

    @Override
    public Rule getRule(Long ruleId) {
        return ruleRepository.findById(ruleId);
    }

    @Override
    public PageInfo<Rule> pageRule(Long projectId, Integer pageNum, Integer pageSize) {
        Page<Rule> rulePage = ruleRepository.pageRule(pageNum, pageSize, projectId);
        List<Rule> records = rulePage.getRecords();
        PageInfo<Rule> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(pageNum);
        pageInfo.setPageSize(records.size());
        pageInfo.setPageTotalNum((int) rulePage.getPages());
        pageInfo.setRecordTotalNum((int) rulePage.getTotal());
        pageInfo.setData(records);
        return pageInfo;
    }
}
