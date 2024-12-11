package com.group6.mapper;

import com.group6.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 插入新用户
     *
     * @param user 用户对象
     */
    @Insert("INSERT INTO `user` (`id`, `username`, `password`, `email`, `avatar`, `role`, `building`, `dormitory`) " +
            "VALUES (#{id}, #{username}, #{password}, #{email}, #{avatar}, #{role}, #{building}, #{dormitory})")
    void insertUser(User user);

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

    @Select("SELECT COUNT(*) FROM user WHERE role = 'ADMIN'")
    int findAdminCount();


    @Select("SELECT * FROM user WHERE role != 'ADMIN'")
    List<User> findAllNonAdminUsers();

    /**
     * 查询指定楼号和宿舍号的非管理员用户
     * @param building 楼号
     * @param dormitory 宿舍号
     * @return 非管理员用户列表
     */
    @Select("SELECT * FROM user WHERE building = #{building} AND dormitory = #{dormitory} AND role != 'ADMIN'")
    List<User> findUsersByBuildingAndRoom(@Param("building") String building, @Param("dormitory") String dormitory);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUserById(String id);

}
