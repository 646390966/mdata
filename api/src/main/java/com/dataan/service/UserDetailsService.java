package com.dataan.service;

import com.dataan.entity.User;
import java.util.Date;
import java.util.List;

/**
 * @author zhan bing liang
 * @date 2024/6/27 15:13
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    /**
     * 新增用户
     * @param user
     * @return
     */
    public User createUser(User user);

    /**
     * 修改用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(String id);

    /**
     * 通过id获取用户
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * 通过名称获取用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 获取用户列表
     * @param page
     * @param size
     * @param username
     * @param createTimeStart
     * @param createTimeEnd
     * @param orderColumn
     * @param desc
     * @return
     */
    List<User> getUserList(Integer page, Integer size, String username, Date createTimeStart, Date createTimeEnd, String orderColumn, Boolean desc);

    /**
     * 获取当前用户
     * @return
     */
    User getCurrentUser();

    /**
     * 获取token
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    String getToken(String username, String password, Boolean rememberMe);

    /**
     * 获取用户总数量
     * @return
     */
    Integer getTotalCount();
}
