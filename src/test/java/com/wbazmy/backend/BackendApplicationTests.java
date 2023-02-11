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

    @Test
    void test() throws IOException {
        Process process = Runtime.getRuntime().exec("git -C " + "D:\\学习\\实验室\\研究生毕设\\backend" + " rev-parse " + "master2");
        String commitId = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
        System.out.println(commitId);

    }

}
