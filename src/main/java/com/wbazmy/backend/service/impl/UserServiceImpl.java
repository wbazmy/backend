package com.wbazmy.backend.service.impl;

import com.wbazmy.backend.dao.UserRepository;
import com.wbazmy.backend.model.dto.UserDto;
import com.wbazmy.backend.model.entity.User;
import com.wbazmy.backend.service.UserService;
import com.wbazmy.backend.utils.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

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
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        user.setPassword(MD5Util.encode(user.getPassword()));
        userRepository.save(user);
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
        user.setPassword(MD5Util.encode(user.getPassword()));
        userRepository.updateByUserId(user);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setLogStatus(Boolean.TRUE);
        return userDto;
    }
}
