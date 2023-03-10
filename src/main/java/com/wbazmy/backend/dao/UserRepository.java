package com.wbazmy.backend.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.mapper.UserMapper;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.model.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/4 - 17:18
 */
@Repository
public class UserRepository {

    @Resource
    private UserMapper userMapper;

    public User findByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        return userMapper.selectOne(queryWrapper);
    }

    public Page<User> pageUser(String userName, Integer pageNum, Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", userName);
        Page<User> page = new Page<>(pageNum, pageSize);
        userMapper.selectPage(page, queryWrapper);
        return page;
    }

    public void save(User user) {
        if (user != null) {
            userMapper.insert(user);
        }
    }

    public void updateByUserId(User user) {
        if (user != null) {
            userMapper.updateById(user);
        }
    }

    public void deleteByUserId(Long userId) {
        userMapper.deleteById(userId);
    }
}
