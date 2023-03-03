package com.wbazmy.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wbazmy.backend.constant.enums.BuildModeEnum;
import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.cron.Job.CheckJob;
import com.wbazmy.backend.cron.Job.UpdateRepoJob;
import com.wbazmy.backend.dao.ProjectRepository;
import com.wbazmy.backend.model.dto.CronJobDto;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.request.CronJobRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/3/2 - 19:57
 */
@RequestMapping("/cron_job")
@Controller
@Slf4j
public class CronJobController {

    @Resource
    private Scheduler scheduler;

    @Resource
    private ProjectRepository projectRepository;

    @PostMapping("/create")
    @ResponseBody
    public ResponseResult createJob(@RequestBody CronJobRequest request) throws SchedulerException {
        if (StringUtils.isBlank(request.getCronExpression()) || StringUtils.isBlank(request.getJobName()) ||
                StringUtils.isBlank(request.getJobType())) {
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }

        if (scheduler.checkExists(new JobKey(request.getJobName()))) {
            return ResponseResult.fail(ResponseCode.CRONJOBNAMEEXIST.getCode(), ResponseCode.CRONJOBNAMEEXIST.getMsg());
        }

        JobDetail jobDetail;
        if (request.getJobType().equals("UPDATE_REPO")) {
            if (StringUtils.isBlank(request.getProjectName())) {
                return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
            }
            jobDetail = JobBuilder.newJob(UpdateRepoJob.class)
                    .withIdentity(request.getJobName())
                    .usingJobData("projectName", request.getProjectName())
                    .build();
        } else {
            if (Objects.isNull(request.getBuildMode()) || Objects.isNull(request.getProjectId())) {
                return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
            }
            jobDetail = JobBuilder.newJob(CheckJob.class)
                    .withIdentity(request.getJobName())
                    .usingJobData("projectId", request.getProjectId())
                    .usingJobData("buildMode", request.getBuildMode().equals(BuildModeEnum.CLEAN_BUILD) ? "CLEAN_BUILD" : "INCREMENTAL_BUILD")
                    .build();
        }

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(request.getJobName())
                .withSchedule(CronScheduleBuilder.cronSchedule(request.getCronExpression()))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        return ResponseResult.success();
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseResult updateJob(@RequestBody CronJobRequest request) throws SchedulerException {
        deleteJob(request.getJobName());
        createJob(request);
        return ResponseResult.success();
    }


    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult deleteJob(@RequestParam String jobName) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName);
        if (scheduler.checkExists(triggerKey)) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(jobName));
        }
        return ResponseResult.success();
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseResult<CronJobDto> getJob(@RequestParam String jobName) throws SchedulerException {
        JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
        if (Objects.isNull(jobDetail)) {
            return ResponseResult.fail(ResponseCode.CRONJOBNOTEXIST.getCode(), ResponseCode.CRONJOBNOTEXIST.getMsg());
        }
        CronJobDto cronJobDto = new CronJobDto();
        cronJobDto.setJobName(jobName);
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        if (jobDetail.getJobClass().equals(UpdateRepoJob.class)) {
            cronJobDto.setProjectName(jobDataMap.getString("projectName"));
            cronJobDto.setJobType("UPDATE_REPO");
        } else {
            String projectName = projectRepository.findById(jobDataMap.getLong("projectId")).getProjectName();
            cronJobDto.setProjectName(projectName);
            cronJobDto.setBuildMode(jobDataMap.getString("buildMode").equals("CLEAN_BUILD") ? BuildModeEnum.CLEAN_BUILD : BuildModeEnum.INCREMENTAL_BUILD);
            cronJobDto.setJobType("CHECK");
        }
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(JobKey.jobKey(jobName));
        if (!triggers.isEmpty()) {
            Trigger trigger = triggers.get(0);
            cronJobDto.setCronExpression(((CronTrigger) trigger).getCronExpression());
            cronJobDto.setCreateTime(trigger.getStartTime());
            cronJobDto.setLastExecutionTime(trigger.getPreviousFireTime());
        }
        return ResponseResult.success(cronJobDto);
    }

    @GetMapping("/get_all")
    @ResponseBody
    public ResponseResult<List<CronJobDto>> getAllJob() throws SchedulerException {
        List<CronJobDto> cronJobDtoList = new ArrayList<>();
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyGroup())) {
            CronJobDto cronJobDto = new CronJobDto();
            cronJobDto.setJobName(jobKey.getName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            if (jobDetail.getJobClass().equals(UpdateRepoJob.class)) {
                cronJobDto.setProjectName(jobDataMap.getString("projectName"));
                cronJobDto.setJobType("UPDATE_REPO");
            } else {
                String projectName = projectRepository.findById(jobDataMap.getLong("projectId")).getProjectName();
                cronJobDto.setProjectName(projectName);
                cronJobDto.setBuildMode(jobDataMap.getString("buildMode").equals("CLEAN_BUILD") ? BuildModeEnum.CLEAN_BUILD : BuildModeEnum.INCREMENTAL_BUILD);
                cronJobDto.setJobType("CHECK");
            }
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (!triggers.isEmpty()) {
                Trigger trigger = triggers.get(0);
                cronJobDto.setCronExpression(((CronTrigger) trigger).getCronExpression());
                cronJobDto.setCreateTime(trigger.getStartTime());
                cronJobDto.setLastExecutionTime(trigger.getPreviousFireTime());
            }
            cronJobDtoList.add(cronJobDto);
        }
        return ResponseResult.success(cronJobDtoList);
    }

}
