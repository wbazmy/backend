package com.wbazmy.backend.interceptor;

import com.wbazmy.backend.utils.JwtUtil;
import com.wbazmy.backend.utils.UserContextUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author Zhang Yang
 * @description
 * @date 2023/2/6 - 14:57
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/user/login".equals(request.getRequestURI()) || "/user/signup".equals(request.getRequestURI())
                || "/history/report_file".equals(request.getRequestURI())) {
            return true;
        }

        // 从请求头中获取token字符串并解析
        Claims claims = JwtUtil.parse(request.getHeader("X-Token"));
        // 已登录就直接放行
        if (claims != null) {
            UserContextUtil.add(claims.getSubject());
            return true;
        }

        // 走到这里就代表是其他接口，且没有登录
        // 设置响应数据类型为json（前后端分离）
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        // 设置响应内容，结束请求
        out.write("Please login");
        out.flush();
        out.close();
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtil.remove();
    }
}
