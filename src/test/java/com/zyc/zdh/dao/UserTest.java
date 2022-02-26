package com.zyc.zdh.dao;

import com.zyc.zdh.ZdhApplication;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes= ZdhApplication.class)
@RunWith(SpringRunner.class)
public class UserTest {
    @Autowired
    private AccountService accountService;
    @Test
    public void testFindPw() {
        final User zzw = new User("zzw", "0105");
        final User byPw = accountService.findByPw(zzw);
        System.out.println(byPw);
    }
}
