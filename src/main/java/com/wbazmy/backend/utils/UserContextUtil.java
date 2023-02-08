package com.wbazmy.backend.utils;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 15:16
 */
public class UserContextUtil {
    private static final ThreadLocal<String> user = new ThreadLocal<>();

    public static void add(String userName) {
        user.set(userName);
    }

    public static void remove() {
        user.remove();
    }

    /**
     * @return 当前登录用户的用户名
     */
    public static String getCurrentUserName() {
        return user.get();
    }
}
