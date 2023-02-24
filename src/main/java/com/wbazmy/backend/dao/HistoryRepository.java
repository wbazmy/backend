package com.wbazmy.backend.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.mapper.HistoryMapper;
import com.wbazmy.backend.model.entity.History;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/8 - 18:27
 */
@Repository
public class HistoryRepository {
    @Resource
    private HistoryMapper historyMapper;

    public void save(History history) {
        if (history != null) {
            historyMapper.insert(history);
        }
    }

    public void updateById(History history) {
        if (history != null) {
            historyMapper.updateById(history);
        }
    }

    public void deleteById(Long id) {
        if (id != null) {
            historyMapper.deleteById(id);
        }
    }

    public History findById(Long id) {
        if (id != null) {
            return historyMapper.selectById(id);
        }
        return null;
    }

    public Page<History> pageHistory(Integer pageNum, Integer pageSize, Long projectId) {
        Page<History> page = new Page<>(pageNum, pageSize);
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        if (!Objects.isNull(projectId)) {
            queryWrapper.eq("project_id", projectId);
        }
        historyMapper.selectPage(page, queryWrapper);
        return page;
    }
}
