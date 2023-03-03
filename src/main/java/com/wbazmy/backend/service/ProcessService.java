package com.wbazmy.backend.service;

import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.model.entity.Project;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/11 - 15:08
 */
public interface ProcessService {
    void callCheck(History history, Project project) throws InterruptedException;

    void cloneRepo(String repoUrl);

    void pullRepo(String projectName);

    void createDir(Project project);
}
