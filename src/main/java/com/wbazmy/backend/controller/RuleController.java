package com.wbazmy.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.entity.Rule;
import com.wbazmy.backend.service.RuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 17:17
 */
@RequestMapping("/rule")
@Controller
@Slf4j
public class RuleController {
    @Resource
    private RuleService ruleService;

    @GetMapping("/page")
    @ResponseBody
    public ResponseResult<PageInfo<Rule>> pageRule(@RequestParam Long projectId, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        if (Objects.isNull(projectId) || Objects.isNull(pageNum) || Objects.isNull(pageSize)) {
            log.info("规则分页查询失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(ruleService.pageRule(projectId, pageNum, pageSize));
    }

    @GetMapping("/info")
    @ResponseBody
    public ResponseResult<Rule> getRule(@RequestParam Long ruleId) {
        if (Objects.isNull(ruleId)) {
            log.info("规则查询失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(ruleService.getRule(ruleId));
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseResult<Rule> createRule(@RequestBody Rule rule) {
        if (StringUtils.isBlank(rule.getRuleName()) || StringUtils.isBlank(rule.getRuleContent()) ||
                Objects.isNull(rule.getProjectId()) || Objects.isNull(rule.getRuleMode())) {
            log.info("规则创建失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        Rule rule1 = ruleService.createRule(rule);
        if (Objects.isNull(rule1)) {
            log.info("规则名称已存在");
            return ResponseResult.fail(ResponseCode.RULENAMEEXIST.getCode(), ResponseCode.RULENAMEEXIST.getMsg());
        }
        return ResponseResult.success(rule1);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseResult<Rule> updateRule(@RequestBody Rule rule) {
        if (Objects.isNull(rule.getId())) {
            log.info("规则更新失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        Rule rule1 = ruleService.updateRule(rule);
        if (Objects.isNull(rule1)) {
            log.info("规则名称已存在");
            return ResponseResult.fail(ResponseCode.RULENAMEEXIST.getCode(), ResponseCode.RULENAMEEXIST.getMsg());
        }
        return ResponseResult.success(rule1);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult deleteRule(@RequestParam Long ruleId) {
        if (Objects.isNull(ruleId)) {
            log.info("规则删除失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        Boolean flag = ruleService.deleteRule(ruleId);
        if (!flag) {
            log.info("规则不存在");
            return ResponseResult.fail(ResponseCode.RULENOTEXIST.getCode(), ResponseCode.RULENOTEXIST.getMsg());
        }
        return ResponseResult.success();
    }
}
