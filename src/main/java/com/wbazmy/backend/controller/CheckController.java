package com.wbazmy.backend.controller;

import com.wbazmy.backend.constant.enums.CheckStatusEnum;
import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.model.request.CheckRequest;
import com.wbazmy.backend.service.CheckService;
import com.wbazmy.backend.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.ognl.OgnlRuntime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 17:17
 */
@RequestMapping("check")
@Controller
@Slf4j
public class CheckController {

    @Resource
    private CheckService checkService;

    @PostMapping("/default_check")
    @ResponseBody
    public ResponseResult<History> defaultCheck(@RequestBody CheckRequest request) {
        if (Objects.isNull(request.getProjectId()) || Objects.isNull(request.getBuildMode())) {
            log.info("请求参数不足，检测失败");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        History history = checkService.depErrorCheck(request);
        if(Objects.isNull(history)) {
            log.info("检测失败");
            return ResponseResult.fail(ResponseCode.CHECKERROR.getCode(), ResponseCode.CHECKERROR.getMsg());
        }
        if (history.getCheckStatus().equals(CheckStatusEnum.FAILED)) {
            log.info("请选择全量构建模式，因为当前项目尚未进行过检测");
            return ResponseResult.fail(ResponseCode.BUILDMODEERROR.getCode(), ResponseCode.BUILDMODEERROR.getMsg());
        }
        return ResponseResult.success(history);
    }
}
