package com.wbazmy.backend.cron.Job;

import com.wbazmy.backend.service.ProcessService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/3/3 - 9:06
 */
@Component
@DisallowConcurrentExecution
public class UpdateRepoJob implements Job {

    @Resource
    private ProcessService processService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String projectName = jobExecutionContext.getJobDetail().getJobDataMap().getString("projectName");
        processService.pullRepo(projectName);
    }
}
