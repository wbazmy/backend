package com.wbazmy.backend.model.dto;

import com.wbazmy.backend.constant.enums.BuildTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 19:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    private Long id;
    private String projectName;
    private String repoUrl;
    private String path;
    private String buildPath;
    private BuildTypeEnum buildType;
    private String description;
    private String lastCommitId;
    private Date createTime;
    private Date updateTime;
}
