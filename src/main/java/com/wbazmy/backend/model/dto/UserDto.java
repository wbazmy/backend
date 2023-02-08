package com.wbazmy.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/4 - 19:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String phoneNumber;
    private String email;
    private String githubHomeUrl;
    private Date createTime;
    private Date updateTime;
    private Boolean logStatus = Boolean.FALSE;
    private String jwtToken;
}
