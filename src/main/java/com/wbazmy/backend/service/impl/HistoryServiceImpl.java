package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.constant.enums.EdgeTypeEnum;
import com.wbazmy.backend.constant.enums.NodeTypeEnum;
import com.wbazmy.backend.dao.HistoryRepository;
import com.wbazmy.backend.dao.ProjectRepository;
import com.wbazmy.backend.dao.UserRepository;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.entity.*;
import com.wbazmy.backend.service.HistoryService;
import com.wbazmy.backend.utils.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 17:01
 */
@Service
@Slf4j
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private HistoryRepository historyRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private UserRepository userRepository;

    @Value("${file.history-data-path}")
    private String historyDataPath;

    @Override
    public void saveHistory(History history) {
        historyRepository.save(history);
    }

    @Override
    public PageInfo<History> pageHistory(String projectName, Integer pageNum, Integer pageSize) {
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        Project project = projectRepository.findByProjectNameAndUserId(projectName, userId);
        Page<History> historyPage = historyRepository.pageHistory(pageNum, pageSize, Objects.isNull(project) ? null : project.getId());
        List<History> records = historyPage.getRecords();
        PageInfo<History> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(pageNum);
        pageInfo.setPageSize(records.size());
        pageInfo.setPageTotalNum((int) historyPage.getPages());
        pageInfo.setRecordTotalNum((int) historyPage.getTotal());
        pageInfo.setData(records);
        return pageInfo;
    }

    @Override
    public List<History> listHistory(String projectName) {
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        Project project = projectRepository.findByProjectNameAndUserId(projectName, userId);
        return historyRepository.listHistory(project.getId());
    }

    @Override
    public Boolean deleteHistory(Long id) {
        History history = historyRepository.findById(id);
        if (history == null) {
            return Boolean.FALSE;
        }
        historyRepository.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public History getHistory(Long id) {
        return historyRepository.findById(id);
    }

    @Override
    public List<Graph> getGraph(Long id, String projectName) {
        String userName = UserContextUtil.getCurrentUserName();
        Long userId = userRepository.findByUserName(userName).getId();
        //todo 待修改
//        String staticFileName = historyDataPath + projectName + "-" + userId + "/" + "static_graph" + "_" + id + ".txt";
        String staticFileName = "D:\\学习\\实验室\\研究生毕设\\static_graph_30.txt";
//        String actualFileName = historyDataPath + projectName + "-" + userId + "/" + "actual_graph" + "_" + id + ".txt";
        String actualFileName = "D:\\学习\\实验室\\研究生毕设\\actual_graph_30.txt";

        File file = new File(staticFileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Graph staticGraph = new Graph();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            String node = parts[0];
            String[] dependencies = parts[1].split(",");
            if (staticGraph.getNodes().containsKey(node)) {
                staticGraph.getNodes().get(node).setNodeType(NodeTypeEnum.OUTPUT);
            } else {
                staticGraph.getNodes().put(node, new Node(node, NodeTypeEnum.OUTPUT));
            }
            for (String dependency : dependencies) {
                if (dependency.startsWith("RD")) {
                    staticGraph.getEdges().add(new Edge(node, dependency.substring(3), EdgeTypeEnum.REDUNDANT));
                    if (!staticGraph.getNodes().containsKey(dependency.substring(3))) {
                        staticGraph.getNodes().put(dependency.substring(3), new Node(dependency.substring(3), NodeTypeEnum.INPUT));
                    }
                } else {
                    staticGraph.getEdges().add(new Edge(node, dependency, EdgeTypeEnum.NORMAL));
                    if (!staticGraph.getNodes().containsKey(dependency)) {
                        staticGraph.getNodes().put(dependency, new Node(dependency, NodeTypeEnum.INPUT));
                    }
                }
            }
        }

        file = new File(actualFileName);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            log.error("文件不存在");
        }
        Graph actualGraph = new Graph();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            String node = parts[0];
            String[] dependencies = parts[1].split(",");
            if (actualGraph.getNodes().containsKey(node)) {
                actualGraph.getNodes().get(node).setNodeType(NodeTypeEnum.OUTPUT);
            } else {
                actualGraph.getNodes().put(node, new Node(node, NodeTypeEnum.OUTPUT));
            }
            for (String dependency : dependencies) {
                if (dependency.startsWith("MD")) {
                    actualGraph.getEdges().add(new Edge(node, dependency.substring(3), EdgeTypeEnum.MISSING));
                    if (!actualGraph.getNodes().containsKey(dependency.substring(3))) {
                        actualGraph.getNodes().put(dependency.substring(3), new Node(dependency.substring(3), NodeTypeEnum.INPUT));
                    }
                } else {
                    actualGraph.getEdges().add(new Edge(node, dependency, EdgeTypeEnum.NORMAL));
                    if (!actualGraph.getNodes().containsKey(dependency)) {
                        actualGraph.getNodes().put(dependency, new Node(dependency, NodeTypeEnum.INPUT));
                    }
                }
            }
        }
        List<Graph> graphs = Arrays.asList(staticGraph, actualGraph);
        scanner.close();
        return graphs;
    }
}
