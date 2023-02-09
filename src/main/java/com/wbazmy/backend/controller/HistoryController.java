package com.wbazmy.backend.controller;

import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 17:02
 */
@RequestMapping("/history")
@Controller
@Slf4j
public class HistoryController {
    @Resource
    private HistoryService historyService;

    @GetMapping("/page")
    @ResponseBody
    public ResponseResult<PageInfo<History>> pageHistory(@RequestParam Long projectId, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        if (Objects.isNull(projectId) || Objects.isNull(pageNum) || Objects.isNull(pageSize)) {
            log.info("参数不足");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(historyService.pageHistory(projectId, pageNum, pageSize));
    }

    @GetMapping("/info")
    @ResponseBody
    public ResponseResult<History> getHistory(@RequestParam Long historyId) {
        if (Objects.isNull(historyId)) {
            log.info("参数不足");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        History history = historyService.getHistory(historyId);
        if (Objects.isNull(history)) {
            log.info("历史记录不存在");
            return ResponseResult.fail(ResponseCode.HISTORYNOTEXIST.getCode(), ResponseCode.HISTORYNOTEXIST.getMsg());
        }
        return ResponseResult.success(history);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult deleteHistory(@RequestParam Long historyId) {
        if (Objects.isNull(historyId)) {
            log.info("参数不足");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        if (!historyService.deleteHistory(historyId)) {
            log.info("历史记录不存在，删除失败");
            return ResponseResult.fail(ResponseCode.HISTORYNOTEXIST.getCode(), ResponseCode.HISTORYNOTEXIST.getMsg());
        }
        return ResponseResult.success();
    }
}
