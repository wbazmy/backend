package com.wbazmy.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ProjectDto;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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

    //todo 待修改
    @Value("${file.upload-path}")
    private String uploadFilePath;

    @Resource
    private ProjectService projectService;

    @GetMapping("/page")
    @ResponseBody
    public ResponseResult<PageInfo<ProjectDto>> pageProject(@RequestParam String projectName, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        if (Objects.isNull(pageNum) || Objects.isNull(pageSize)) {
            log.info("项目分页查询失败，参数不完整");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(projectService.pageProject(projectName, pageNum, pageSize));
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
        if (StringUtils.isBlank(project.getProjectName()) || StringUtils.isBlank(project.getRepoUrl())
                || StringUtils.isBlank(project.getMainBranch()) || Objects.isNull(project.getBuildType())) {
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

    @PostMapping("/upload")
    @ResponseBody
    public ResponseResult httpUpload(@RequestParam("scriptFile") MultipartFile[] files, @RequestParam String projectName) {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();  // 文件名
            File dest = new File(uploadFilePath + projectName + "/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("{}文件上传成功", projectName);
        return ResponseResult.success();
    }
}
