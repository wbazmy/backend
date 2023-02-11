package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wbazmy.backend.constant.enums.BuildModeEnum;
import com.wbazmy.backend.constant.enums.CheckStatusEnum;
import com.wbazmy.backend.constant.enums.RuleModeEnum;
import com.wbazmy.backend.dao.HistoryRepository;
import com.wbazmy.backend.dao.ProjectRepository;
import com.wbazmy.backend.dao.RuleRepository;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.model.entity.Rule;
import com.wbazmy.backend.model.request.CheckRequest;
import com.wbazmy.backend.service.CheckService;
import com.wbazmy.backend.service.ProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/10 - 15:17
 */
@Service
@Slf4j
public class CheckServiceImpl implements CheckService {

    @Resource
    private HistoryRepository historyRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private ProcessService processService;

    @Override
    public History depErrorCheck(CheckRequest request) {
        History history = new History();
        Project project = projectRepository.findById(request.getProjectId());
        history.setProjectId(request.getProjectId());
        history.setBuildMode(request.getBuildMode());
        history.setStartTime(new Date());
        String curCommitId = "";
        try {
            curCommitId = getCommitId(request.getCommitId(), request.getBranch(), project.getPath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        history.setCurCommitId(curCommitId);
        if (StringUtils.isBlank(project.getLastCommitId()) && request.getBuildMode().equals(BuildModeEnum.INCREMENTAL_BUILD)) {
            history.setEndTime(new Date());
            history.setDuration(0);
            history.setCheckStatus(CheckStatusEnum.FAILED);
            history.setBaseCommitId(curCommitId);
            historyRepository.save(history);
            return history;
        }
        history.setCheckStatus(CheckStatusEnum.CHECKING);
        if (request.getBuildMode().equals(BuildModeEnum.INCREMENTAL_BUILD)) {
            history.setBaseCommitId(project.getLastCommitId());
        } else {
            history.setBaseCommitId(curCommitId);
        }
        historyRepository.save(history);
        try {
            processService.callCheck(history, project);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return history;
    }

    private String getCommitId(String commitId, String branch, String projectPath) throws IOException {
        if (StringUtils.isBlank(commitId)) {
            Process process = Runtime.getRuntime().exec("git -C " + projectPath + " rev-parse " + branch);
            commitId = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
            if (commitId.equals(branch)) {
                log.error("Branch {} does not exist", branch);
            }
        }
        return commitId;
    }

}