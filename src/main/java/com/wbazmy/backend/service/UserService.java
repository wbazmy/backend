package com.wbazmy.backend.service;

import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.UserDto;
import com.wbazmy.backend.model.entity.User;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/4 - 17:19
 */
public interface UserService {

    UserDto userLogIn(String userName, String password);

    UserDto userRegister(User user);

    UserDto userInfo(String userName);

    UserDto userUpdate(User user);

    void userDelete(Long userId);

    PageInfo<UserDto> pageUser(String userName, Integer pageNum, Integer pageSize);
}
