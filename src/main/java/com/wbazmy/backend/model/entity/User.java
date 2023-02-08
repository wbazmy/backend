package com.wbazmy.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/3 - 20:55
 */
@Data
@TableName("user")
public class User extends PublicField {
    private String userName;
    private String password;
    private String phoneNumber;
    private String email;
    private String githubHomeUrl;

}
