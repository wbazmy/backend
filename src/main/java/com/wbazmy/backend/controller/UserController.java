package com.wbazmy.backend.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wbazmy.backend.constant.enums.ResponseCode;
import com.wbazmy.backend.model.dto.PageInfo;
import com.wbazmy.backend.model.dto.ResponseResult;
import com.wbazmy.backend.model.dto.UserDto;
import com.wbazmy.backend.model.entity.User;
import com.wbazmy.backend.service.UserService;
import com.wbazmy.backend.utils.JwtUtil;
import com.wbazmy.backend.utils.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/4 - 17:20
 */
@RequestMapping("/user")
@Controller
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseResult userSignUp(@RequestBody User user) {
        if (StringUtils.isBlank(user.getUserName()) || StringUtils.isBlank(user.getPassword())) {
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        UserDto userDto = userService.userRegister(user);
        if (userDto == null) {
            log.info("用户名已存在");
            return ResponseResult.fail(ResponseCode.USERNAMEEXIST.getCode(), ResponseCode.USERNAMEEXIST.getMsg());
        }
        userDto.setToken(JwtUtil.generate(userDto.getUserName()));
        log.info(userDto.getUserName() + "注册成功");
        return ResponseResult.success(userDto);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseResult<UserDto> userLogIn(@RequestParam String userName, @RequestParam String password) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            log.info("用户名或密码为空");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        UserDto userDto = userService.userLogIn(userName, password);
        if (userDto == null) {
            log.info("用户名不存在");
            return ResponseResult.fail(ResponseCode.USERNOTEXIST.getCode(), ResponseCode.USERNOTEXIST.getMsg());
        }
        if (userDto.getLogStatus().equals(Boolean.FALSE)) {
            log.info("密码错误");
            return ResponseResult.fail(ResponseCode.WRONGPASSWORD.getCode(), ResponseCode.WRONGPASSWORD.getMsg(), userDto);
        }
        userDto.setToken(JwtUtil.generate(userDto.getUserName()));
        log.info(userName + "登录成功");
        return ResponseResult.success(userDto);
    }

    @GetMapping("/info")
    @ResponseBody
    public ResponseResult<UserDto> userInfo() {
        UserDto userDto = userService.userInfo(UserContextUtil.getCurrentUserName());
        if (userDto == null) {
            log.info("用户名不存在");
            return ResponseResult.fail(ResponseCode.USERNOTEXIST.getCode(), ResponseCode.USERNOTEXIST.getMsg());
        }
        log.info("用户信息请求成功");
        return ResponseResult.success(userDto);
    }

    @GetMapping("/page")
    @ResponseBody
    public ResponseResult<PageInfo<UserDto>> pageUser(@RequestParam(defaultValue = "") String userName, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize) {
        log.info("分页查询用户信息");
        return ResponseResult.success(userService.pageUser(userName, pageNum, pageSize));
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseResult<UserDto> userUpdate(@RequestBody User user) {
        if (StringUtils.isBlank(user.getUserName())) {
            user.setUserName(UserContextUtil.getCurrentUserName());
        }
        UserDto userDto = userService.userUpdate(user);
        if (userDto == null) {
            log.info("用户名不存在");
            return ResponseResult.fail(ResponseCode.USERNOTEXIST.getCode(), ResponseCode.USERNOTEXIST.getMsg());
        }
        log.info(userDto.getUserName() + "更新成功");
        return ResponseResult.success(userDto);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult userDelete(@RequestParam Long userId) {
        if(Objects.isNull(userId)) {
            log.info("用户id为空，删除失败");
            return ResponseResult.fail(ResponseCode.MISSCONTENT.getCode(), ResponseCode.MISSCONTENT.getMsg());
        }
        userService.userDelete(userId);
        log.info("用户删除成功");
        return ResponseResult.success();
    }
}
