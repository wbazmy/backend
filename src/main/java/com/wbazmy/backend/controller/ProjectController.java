package com.wbazmy.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ProjectDto;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 17:16
 */
@RequestMapping("/project")
@Controller
@Slf4j
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/page")
    @ResponseBody
    public ResponseResult<PageInfo<ProjectDto>> pageProject(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        if (Objects.isNull(pageNum) || Objects.isNull(pageSize)) {
            log.info("项目分页查询失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(projectService.pageProject(pageNum, pageSize));
    }

    @GetMapping("/info")
    @ResponseBody
    public ResponseResult<ProjectDto> getProjectInfo(@RequestParam String projectName) {
        if (StringUtils.isBlank(projectName)) {
            log.info("项目信息获取失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        ProjectDto projectDto = projectService.getProjectInfo(projectName);
        if (Objects.isNull(projectDto)) {
            log.info("项目信息获取失败，项目不存在");
            return ResponseResult.fail(ResponseCode.PROJECTNAMENOTEXIST.getCode(), ResponseCode.PROJECTNAMENOTEXIST.getMsg());
        }
        return ResponseResult.success(projectDto);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseResult<ProjectDto> createProject(@RequestBody Project project) {
        if (StringUtils.isBlank(project.getProjectName()) || StringUtils.isBlank(project.getBuildPath()) ||
                StringUtils.isBlank(project.getPath()) || StringUtils.isBlank(project.getRepoUrl()) ||
                Objects.isNull(project.getBuildType())) {
            log.info("项目创建失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        ProjectDto projectDto = projectService.createProject(project);
        if (Objects.isNull(projectDto)) {
            log.info("项目名称已存在");
            return ResponseResult.fail(ResponseCode.PROJECTNAMEEXIST.getCode(), ResponseCode.PROJECTNAMEEXIST.getMsg());
        }
        return ResponseResult.success(projectDto);
    }


    @PostMapping("/update")
    @ResponseBody
    public ResponseResult<ProjectDto> updateProject(@RequestBody Project project) {
        if (Objects.isNull(project.getId())) {
            log.info("项目更新失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        ProjectDto projectDto = projectService.updateProject(project);
        if (Objects.isNull(projectDto)) {
            log.info("项目名称已存在");
            return ResponseResult.fail(ResponseCode.PROJECTNAMEEXIST.getCode(), ResponseCode.PROJECTNAMEEXIST.getMsg());
        }
        return ResponseResult.success(projectDto);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult deleteProject(@RequestParam String projectName) {
        if (StringUtils.isBlank(projectName)) {
            log.info("项目删除失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        Boolean flag = projectService.deleteProject(projectName);
        if (flag.equals(Boolean.FALSE)) {
            log.info("项目删除失败，项目不存在");
            return ResponseResult.fail(ResponseCode.PROJECTNAMENOTEXIST.getCode(), ResponseCode.PROJECTNAMENOTEXIST.getMsg());
        }
        return ResponseResult.success();
    }
}
