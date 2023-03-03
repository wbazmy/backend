package com.wbazmy.backend.cron.Job;

import com.wbazmy.backend.constant.enums.BuildModeEnum;
import com.wbazmy.backend.model.request.CheckRequest;
import com.wbazmy.backend.service.CheckService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/3/3 - 9:07
 */
@Component
@DisallowConcurrentExecution
public class CheckJob implements Job {
    @Resource
    private CheckService checkService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        CheckRequest request = new CheckRequest();
        Long projectId = jobExecutionContext.getJobDetail().getJobDataMap().getLong("projectId");
        String buildMode = jobExecutionContext.getJobDetail().getJobDataMap().getString("buildMode");
        // 保证datamap中存在
        request.setProjectId(projectId);
        request.setBuildMode(buildMode.equals("CLEAN_BUILD") ? BuildModeEnum.CLEAN_BUILD : BuildModeEnum.INCREMENTAL_BUILD);
        request.setIsCronJob(Boolean.TRUE);
        checkService.depErrorCheck(request);
    }
}
