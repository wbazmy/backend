package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.dao.HistoryRepository;
import com.wbazmy.backend.dao.ProjectRepository;
import com.wbazmy.backend.dao.UserRepository;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.service.HistoryService;
import com.wbazmy.backend.utils.UserContextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 17:01
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private HistoryRepository historyRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private UserRepository userRepository;

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
}
