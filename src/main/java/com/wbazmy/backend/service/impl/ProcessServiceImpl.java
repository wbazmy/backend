package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import java.io.*;
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

    @Value("${file.python-path}")
    private String pythonPath;

    @Value("${file.history-data-path}")
    private String historyDataPath;

    @Value("${file.project-path}")
    private String projectPath;

    @Value("${git.mirror-url}")
    private String mirrorUrl;

    @Value("${file.check-data-path}")
    private String checkDataPath;

    @Resource
    private HistoryRepository historyRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private RuleRepository ruleRepository;

    @Override
    @Async
    public void callCheck(History history, Project project) throws InterruptedException {
        // 获取规则信息，然后调用python程序并更新历史记录信息，最后还要更新项目的lastcommitid
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
        // todo 修改buildpath
        String buildpath = projectPath + project.getProjectName();
        if (StringUtils.isNotBlank(project.getBuildPath())) {
            buildpath = buildpath + "/" + project.getBuildPath();
        }
        String command = pythonPath + " " + pythonFilePath + " --build_path=" + buildpath +
                " --project_name=" + project.getProjectName() + " --exclude_target=" + excludeTarget +
                " --exclude_path=" + excludePath + " --exclude_suffix=" + excludeSuffix +
                " --build_mode=" + history.getBuildMode() + " --history_id=" + history.getId() +
                " --user_id=" + project.getUserId();
        if (StringUtils.isNotBlank(history.getBaseCommitId())) {
            command += " --base_commit_id=" + history.getBaseCommitId();
        }
        if (StringUtils.isNotBlank(history.getHeadCommitId())) {
            command += " --head_commit_id=" + history.getHeadCommitId();
        }
        log.info("command:{}", command);

        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStream is1 = process.getInputStream();
            InputStream is2 = process.getErrorStream();
            // 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            new Thread(() -> {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
                try {
                    while (br1.readLine() != null) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
                try {
                    while (br2.readLine() != null) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            int re = process.waitFor();
            if (re == 1) {
                log.info("调用python脚本失败");
                failHandle(history);
                return;
            } else {
                log.info("调用成功");
            }
        } catch (IOException e) {
            failHandle(history);
            log.error(e.getMessage(), e);
            return;
        }

        Integer mdNum;
        Integer rdNum;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(historyDataPath + project.getProjectName() +
                    "-" + project.getUserId() + "/check_num.txt"));
            mdNum = Integer.valueOf(reader.readLine().trim());
            rdNum = Integer.valueOf(reader.readLine().trim());
            log.info("mdNum:{}, rdNum:{}", mdNum, rdNum);
        } catch (IOException e) {
            failHandle(history);
            log.error(e.getMessage(), e);
            return;
        }
        project.setLastCommitId(history.getHeadCommitId());
        projectRepository.updateById(project);
        history.setMdNum(mdNum);
        history.setRdNum(rdNum);
        history.setCheckStatus(CheckStatusEnum.FINISHED);
        history.setEndTime(new Date());
        history.setDuration((int) ((history.getEndTime().getTime() - history.getStartTime().getTime()) / 1000));
        historyRepository.updateById(history);
    }

    @Override
    @Async
    public void cloneRepo(String repoUrl) {
        int lastSlashIndex = repoUrl.lastIndexOf("/"); // 查找最后一个斜杠的位置
        String repoName = repoUrl.substring(lastSlashIndex + 1); // 从该位置开始提取字符串
        if (!repoUrl.endsWith(".git")) {
            repoUrl += ".git";
        }
        repoUrl = repoUrl.replace("github.com", mirrorUrl);
        String[] cmd = {"git", "clone", repoUrl, projectPath + repoName};
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((reader.readLine()) != null) {
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Git clone completed successfully.");
            } else {
                log.error("Git clone failed with exit code " + exitCode + ".");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createDir(Project project) {
        String dirName = checkDataPath + project.getProjectName();
        File directory = new File(dirName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        dirName = historyDataPath + project.getProjectName() + "-" + project.getUserId();
        directory = new File(dirName);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }


    private void failHandle(History history) {
        history.setCheckStatus(CheckStatusEnum.FAILED);
        history.setEndTime(new Date());
        history.setDuration((int) ((history.getEndTime().getTime() - history.getStartTime().getTime()) / 1000));
        historyRepository.updateById(history);
    }
}
