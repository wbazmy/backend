package com.wbazmy.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wbazmy.backend.dao.UserRepository;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ProjectDto;
import com.wbazmy.backend.model.dto.UserDto;
import com.wbazmy.backend.model.entity.Project;
import com.wbazmy.backend.model.entity.User;
import com.wbazmy.backend.service.UserService;
import com.wbazmy.backend.utils.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/4 - 18:37
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDto userLogIn(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        UserDto userDto = new UserDto();
        userDto.setUserName(userName);
        // 用户不存在
        if (user == null) {
            return null;
        }
        // 密码不正确
        if (!Objects.equals(MD5Util.encode(password), user.getPassword())) {
            return userDto;
        }
        BeanUtils.copyProperties(user, userDto);
        userDto.setLogStatus(Boolean.TRUE);
        return userDto;
    }

    @Override
    public UserDto userRegister(User user) {
        String userName = user.getUserName();
        User oldUser = userRepository.findByUserName(userName);
        if (oldUser != null) {
            return null;
        }
        user.setPassword(MD5Util.encode(user.getPassword()));
        userRepository.save(user);
        user = userRepository.findByUserName(userName);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setLogStatus(Boolean.TRUE);
        return userDto;
    }

    @Override
    public UserDto userInfo(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setLogStatus(Boolean.TRUE);
        return userDto;
    }

    @Override
    public UserDto userUpdate(User user) {
        User oldUser = userRepository.findByUserName(user.getUserName());
        if (oldUser == null) {
            return null;
        }
        user.setId(oldUser.getId());
        if (user.getPassword() != null) {
            user.setPassword(MD5Util.encode(user.getPassword()));
        }
        userRepository.updateByUserId(user);
        user = userRepository.findByUserName(user.getUserName());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setLogStatus(Boolean.TRUE);
        return userDto;
    }

    @Override
    public void userDelete(Long userId) {
        userRepository.deleteByUserId(userId);
    }

    @Override
    public PageInfo<UserDto> pageUser(String userName, Integer pageNum, Integer pageSize) {
        Page<User> userPage = userRepository.pageUser(userName, pageNum, pageSize);
        List<User> records = userPage.getRecords();
        List<UserDto> userDtos = records.stream().map(user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }).collect(Collectors.toList());
        PageInfo<UserDto> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(pageNum);
        pageInfo.setPageSize(userDtos.size());
        pageInfo.setPageTotalNum((int) userPage.getPages());
        pageInfo.setRecordTotalNum((int) userPage.getTotal());
        pageInfo.setData(userDtos);
        return pageInfo;
    }
}
