package com.wbazmy.backend.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.mapper.ProjectMapper;
import com.wbazmy.backend.model.entity.Project;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 19:17
 */
@Repository
public class ProjectRepository {

    @Resource
    private ProjectMapper projectMapper;

    public void save(Project project) {
        if (project != null) {
            projectMapper.insert(project);
        }
    }

    public void updateByProjectId(Project project) {
        if (project != null) {
            projectMapper.updateById(project);
        }
    }

    public Project findById(Long id) {
        if (id == null) {
            return null;
        }
        return projectMapper.selectById(id);
    }

    public Project findByProjectName(String name) {
        if (name == null) {
            return null;
        }
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", name);
        return projectMapper.selectOne(queryWrapper);
    }

    public Project findByProjectNameAndUserId(String name, Long userId) {
        if (name == null || userId == null) {
            return null;
        }
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", name).eq("user_id", userId);
        return projectMapper.selectOne(queryWrapper);
    }

    public void deleteByProjectId(Long id) {
        projectMapper.deleteById(id);
    }

    public Page<Project> pageProject(Integer pageNum, Integer pageSize, Long userId) {
        Page<Project> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        projectMapper.selectPage(page, queryWrapper);
        return page;
    }


}
