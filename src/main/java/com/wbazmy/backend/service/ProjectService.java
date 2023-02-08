package com.wbazmy.backend.service;

import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ProjectDto;
import com.wbazmy.backend.model.entity.Project;

import java.util.List;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 19:16
 */
public interface ProjectService {
    ProjectDto createProject(Project project);

    ProjectDto getProjectInfo(String projectName);

    ProjectDto updateProject(Project project);

    Boolean deleteProject(String projectName);

    PageInfo<ProjectDto> pageProject(Integer pageNum, Integer pageSize);
}
