package com.wbazmy.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wbazmy.backend.constant.enums.BuildTypeEnum;
import lombok.Data;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/3 - 20:53
 */
@Data
@TableName("project")
public class Project extends PublicField{
    private String projectName;
    private String repoUrl;
    private Long userId;
    private String path;
    private String buildPath;
    private BuildTypeEnum buildType;
    private String description;
    private String lastCommitId;

}
