package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.event.entity.po.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController controller;
    @Test
    void findAll() {
        List<User> all = controller.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }
}