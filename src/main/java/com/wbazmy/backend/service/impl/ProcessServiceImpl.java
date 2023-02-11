package com.wbazmy.backend.service.impl;

import com.wbazmy.backend.constant.enums.CheckStatusEnum;
import com.wbazmy.backend.constant.enums.RuleModeEnum;
import com.wbazmy.backend.dao.HistoryRepository;
import com.wbazmy.backend.dao.ProjectRepository;
import com.wbazmy.backend.dao.RuleRepository;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.model.entity.Rule;
import com.wbazmy.backend.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/11 - 15:08
 */
@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    @Value("${file.python-file-path}")
    private String pythonFilePath;

    @Resource
    private HistoryRepository historyRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private RuleRepository ruleRepository;

    @Override
    @Async
    public void callCheck(History history, Project project) throws InterruptedException {
        //todo 获取规则信息，然后调用python程序并更新历史记录信息，最后还要更新项目的lastcommitid
        List<Rule> rules = ruleRepository.listRuleByProjectId(history.getProjectId());
        // 规则解析
        StringBuilder excludeTarget = new StringBuilder();
        StringBuilder excludePath = new StringBuilder();
        StringBuilder excludeSuffix = new StringBuilder();
        for (Rule rule : rules) {
            if (rule.getRuleMode().equals(RuleModeEnum.EXCLUDE_TARGET)) {
                excludeTarget.append(rule.getRuleContent().trim()).append(';');
            }
            if (rule.getRuleMode().equals(RuleModeEnum.EXCLUDE_PATH)) {
                excludePath.append(rule.getRuleContent().trim()).append(';');
            }
            if (rule.getRuleMode().equals(RuleModeEnum.EXCLUDE_SUFFIX)) {
                excludeSuffix.append(rule.getRuleContent().trim()).append(';');
            }
        }
        String command = "python " + pythonFilePath + " --build_path=" + project.getBuildPath() +
                " --project_name=" + project.getProjectName() + " --exclude_target=" + excludeTarget +
                " --exclude_path=" + excludePath + " --exclude_suffix=" + excludeSuffix +
                " --base_commit_id=" + history.getBaseCommitId() + " --cur_commit_id=" +
                history.getCurCommitId() + " --build_mode=" + history.getBuildMode();
        log.info("command:{}", command);
        Thread.sleep(3000);
        project.setLastCommitId(history.getCurCommitId());
        projectRepository.updateById(project);
        history.setMdNum(0); // 待修改
        history.setRdNum(0); // 待修改
        history.setCheckStatus(CheckStatusEnum.FINISHED);
        history.setEndTime(new Date());
        history.setDuration((int) ((history.getEndTime().getTime() - history.getStartTime().getTime()) / 1000));
        historyRepository.updateById(history);
    }
}
