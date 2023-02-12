package com.wbazmy.backend;

import com.wbazmy.backend.mapper.UserMapper;
import com.wbazmy.backend.service.impl.CheckServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootTest
class BackendApplicationTests {

    @Resource
    private UserMapper userMapper;


    @Test
    void contextLoads() {
    }


}
