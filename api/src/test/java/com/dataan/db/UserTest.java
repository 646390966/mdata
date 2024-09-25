package com.dataan.db;

import com.dataan.entity.User;
import com.dataan.service.UserDetailsServiceImpl;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhan bing liang
 * @date 2024/6/4 16:42
 */
@SpringBootTest
public class UserTest {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void createUserTest() {

            User user = userDetailsService.createUser(getUser());
            Assertions.assertThat(user.getId()).isNotEmpty();


    }
    @Test
    void getUserList() {
        List<User> userList = userDetailsService.getUserList(1, 10, null,null,null, null, null);
        Assertions.assertThat(userList).isNotEmpty();
    }
    @Test
    void getUserById() {
        User user  = userDetailsService.getUserById("1");
        Assertions.assertThat(user).isNotNull();
    }


    @Test
    void getUserByUsername() {
        User user  = userDetailsService.getUserByUsername("123");


        Assertions.assertThat(user).isNotNull();
    }

    private User getUser() {
        User user = new User();

        user.setUsername("admin2");
        user.setPassword(Base64.getEncoder().encodeToString(new String("admin").getBytes()));
        try {
            String url = "C:\\Users\\战炳良\\Desktop\\bj.JPG";
            Path path = Paths.get(url);
            byte[] bytes = Files.readAllBytes(path);
            String encode = Base64.getEncoder().encodeToString(bytes);
//            user.setAvator(encode);

            user.setRole(List.of("breadcrumb","user","user:add", "user:edit"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
