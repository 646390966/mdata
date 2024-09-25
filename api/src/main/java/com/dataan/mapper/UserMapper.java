package com.dataan.mapper;

import com.dataan.entity.User;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

/**
 * @author zhan bing liang
 * @date 2024/6/4 14:48
 */
@Mapper
public interface UserMapper {
    /**
     * 新增用户
     * @param user
     */
    public void createUser(User user);

    /**
     * 修改用户
     * @param user
     */
    public void updateUser(User user);

    /**
     * 删除用户
     * @param id
     */
    public void deleteUser(String id);

    /**
     * 通过id获取用户
     * @param id
     * @return
     */
    public  User getUserById(String id);

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    public  User getUserByUsername(String username);

    /**
     * 获取用户列表
     * @param rowBounds
     * @param username
     * @param createTimeStart
     * @param createTimeEnd
     * @param orderColumn
     * @param desc
     * @return
     */
    public List<User> getUserList(RowBounds rowBounds, String username, Date createTimeStart, Date createTimeEnd, String orderColumn, Boolean desc);

    /**
     * 获取用户总数量
     * @return
     */
    Integer getTotalCount();
}
