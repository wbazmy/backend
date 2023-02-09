package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.dao.HistoryRepository;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.entity.History;
import com.wbazmy.backend.service.HistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 17:01
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private HistoryRepository historyRepository;

    @Override
    public void saveHistory(History history) {
        historyRepository.save(history);
    }

    @Override
    public PageInfo<History> pageHistory(Long projectId, Integer pageNum, Integer pageSize) {
        Page<History> historyPage = historyRepository.pageHistory(pageNum, pageSize, projectId);
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
        if (history==null) {
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
