package com.wbazmy.backend.model.request;

import com.wbazmy.backend.constant.enums.BuildModeEnum;
import lombok.Data;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/9 - 18:51
 */
@Data
public class CheckRequest {
    private Long projectId;
    private BuildModeEnum buildMode;
    private String commitId;
    private String branch;

}
