package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.dao.ProjectRepository;
import com.wbazmy.backend.dao.UserRepository;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ProjectDto;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.service.ProjectService;
import com.wbazmy.backend.utils.UserContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 19:16
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Value("${file.project-path}")
    private String projectPath;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private UserRepository userRepository;


    @Override
    public ProjectDto createProject(Project project) {
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        project.setUserId(userId);
        project.setPath(projectPath + project.getProjectName());
        project.setBuildPath(projectPath + project.getProjectName() + "/" + project.getBuildPath());
        Project oldProject = projectRepository.findByProjectNameAndUserId(project.getProjectName(), userId);
        if (oldProject != null) {
            return null;
        }
        projectRepository.save(project);
        ProjectDto projectDto = new ProjectDto();
        BeanUtils.copyProperties(project, projectDto);
        // todo 在此还要从github拉取项目到本地
        return projectDto;
    }

    @Override
    public ProjectDto getProjectInfo(String projectName) {
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        Project project = projectRepository.findByProjectNameAndUserId(projectName, userId);
        if (project == null) {
            return null;
        }
        ProjectDto projectDto = new ProjectDto();
        BeanUtils.copyProperties(project, projectDto);
        return projectDto;
    }

    @Override
    public ProjectDto updateProject(Project project) {
        Project oldProject = projectRepository.findById(project.getId());
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        if (oldProject == null) {
            return null;
        }
        if (!Objects.isNull(project.getProjectName()) && !Objects.equals(project.getProjectName(), oldProject.getProjectName())) {
            Project oldProject1 = projectRepository.findByProjectNameAndUserId(project.getProjectName(), userId);
            if (oldProject1 != null) {
                return null;
            }
        }
        if (StringUtils.isNotBlank(project.getBuildPath())) {
            project.setBuildPath(projectPath + project.getProjectName() + "/" + project.getBuildPath());
        }
        projectRepository.updateById(project);
        project = projectRepository.findById(project.getId());
        ProjectDto projectDto = new ProjectDto();
        BeanUtils.copyProperties(project, projectDto);
        return projectDto;
    }

    @Override
    public Boolean deleteProject(String projectName) {
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        Project project = projectRepository.findByProjectNameAndUserId(projectName, userId);
        if (project == null) {
            return Boolean.FALSE;
        }
        projectRepository.deleteByProjectId(project.getId());
        return Boolean.TRUE;
    }

    @Override
    public PageInfo<ProjectDto> pageProject(Integer pageNum, Integer pageSize) {
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        Page<Project> projectPage = projectRepository.pageProject(pageNum, pageSize, userId);
        List<Project> records = projectPage.getRecords();
        List<ProjectDto> projectDtos = records.stream().map(project -> {
            ProjectDto projectDto = new ProjectDto();
            BeanUtils.copyProperties(project, projectDto);
            return projectDto;
        }).collect(Collectors.toList());
        PageInfo<ProjectDto> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(pageNum);
        pageInfo.setPageSize(projectDtos.size());
        pageInfo.setPageTotalNum((int) projectPage.getPages());
        pageInfo.setRecordTotalNum((int) projectPage.getTotal());
        pageInfo.setData(projectDtos);
        return pageInfo;
    }
}
