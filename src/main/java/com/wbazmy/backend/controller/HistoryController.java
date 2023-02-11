package com.wbazmy.backend.controller;

import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    //todo 修改文件路径
    @Value("${file.download-path}")
    private String downLoadPath;

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

    @GetMapping("/report_file")
    @ResponseBody
    public String getReportFile(HttpServletResponse response, @RequestParam Long historyId) {
        if (Objects.isNull(historyId)) {
            log.info("参数不足");
            return "参数不足";
        }
        String fileName = "dep_error_report_" + historyId + ".txt";
        String filePath = downLoadPath + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            log.info("文件不存在");
            return "文件不存在";
        }

        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            log.error("{}", e);
            return "下载失败";
        }
        return "下载成功";
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
