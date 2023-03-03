package com.wbazmy.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.dao.ProjectRepository;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.entity.Graph;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
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

    @Value("${file.history-data-path}")
    private String downLoadPath;

    @Resource
    private HistoryService historyService;

    @Resource
    private ProjectRepository projectRepository;

    @GetMapping("/page")
    @ResponseBody
    public ResponseResult<PageInfo<History>> pageHistory(@RequestParam String projectName, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        if (Objects.isNull(projectName) || Objects.isNull(pageNum) || Objects.isNull(pageSize)) {
            log.info("参数不足");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(historyService.pageHistory(projectName, pageNum, pageSize));
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseResult<List<History>> listHistory(@RequestParam String projectName) {
        if (StringUtils.isBlank(projectName)) {
            log.info("参数不足");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(historyService.listHistory(projectName));
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

    @GetMapping("/graph")
    @ResponseBody
    public ResponseResult<List<Graph>> getGraph(@RequestParam Long historyId, @RequestParam String projectName) {
        if (Objects.isNull(historyId) || StringUtils.isBlank(projectName)) {
            log.info("参数不足");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        return ResponseResult.success(historyService.getGraph(historyId, projectName));
    }

    @GetMapping("/report_file")
    @ResponseBody
    public String getReportFile(HttpServletResponse response, @RequestParam Long projectId, @RequestParam Long historyId) {
        if (Objects.isNull(historyId) || Objects.isNull(projectId)) {
            log.info("参数不足");
            return "参数不足";
        }

        Project project = projectRepository.findById(projectId);
        String fileName = "dep_error_" + historyId + ".csv";
        String filePath = downLoadPath + project.getProjectName() + "-" + project.getUserId() + "/" + fileName;
//        String filePath = "C:\\Users\\23954\\Desktop\\1.csv";
        File file = new File(filePath);
        if (!file.exists()) {
            log.info("{}文件不存在", filePath);
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
