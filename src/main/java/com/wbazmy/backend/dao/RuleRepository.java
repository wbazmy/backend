package com.wbazmy.backend.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.mapper.RuleMapper;
import com.wbazmy.backend.model.entity.Rule;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 20:26
 */
@Repository
public class RuleRepository {

    @Resource
    private RuleMapper ruleMapper;

    public Rule findByRuleNameAndProjectId(String ruleName, Long projectId) {
        QueryWrapper<Rule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rule_name", ruleName).eq("project_id", projectId);
        return ruleMapper.selectOne(queryWrapper);
    }

    public Rule findById(Long ruleId) {
        return ruleMapper.selectById(ruleId);
    }

    public Page<Rule> pageRule(Integer pageNum, Integer pageSize, Long projectId) {
        Page<Rule> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Rule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        return ruleMapper.selectPage(page, queryWrapper);
    }

    public void saveRule(Rule rule) {
        ruleMapper.insert(rule);
    }

    public void updateRule(Rule rule) {
        ruleMapper.updateById(rule);
    }

    public void deleteRule(Long ruleId) {
        ruleMapper.deleteById(ruleId);
    }


}
