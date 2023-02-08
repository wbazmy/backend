package com.wbazmy.backend.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/4 - 17:55
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "Request success"), FAIL(400, "Request fail"), MISSCONTENT(600, "Missing some content"),
    USERNOTEXIST(601, "User not Exist"), WRONGPASSWORD(602, "Password is wrong"),
    USERNAMEEXIST(603, "User name has existed"), WRONGEMAIL(604, "Email format is wrong"),
    WRONGPHONENUMBER(605, "Phong number format is wrong"), ILLEGALACCESS(606, "Illegal access"),
    PROJECTNAMEEXIST(701, "Project name has existed"), PROJECTNAMENOTEXIST(702, "Project name not Exist");

    private final Integer code;

    private final String msg;
}
