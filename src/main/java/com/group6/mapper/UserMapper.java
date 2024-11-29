package com.group6.mapper;

import com.group6.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    /**
     * 插入新用户
     *
     * @param user 用户对象
     */
    @Insert("INSERT INTO user (id, username, password, email, avatar) " +
            "VALUES (#{user.id}, #{user.username}, #{user.password}, #{user.email}, #{user.avatar})")
    void insertUser(@Param("user") User user);


    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    /**
     * 根据用户ID查找用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findUserById(@Param("id") String id);

    /**
     * 更新用户头像
     *
     * @param id 用户ID
     * @param avatar 头像路径
     */
    @Update("UPDATE user SET avatar = #{avatar} WHERE id = #{id}")
    void updateUserAvatar(@Param("id") String id, @Param("avatar") String avatar);
}
