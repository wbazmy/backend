package com.wbazmy.backend;

import com.wbazmy.backend.model.entity.User;
import com.wbazmy.backend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSql() {
        List<User> users = userMapper.selectList(null);


    }

}
