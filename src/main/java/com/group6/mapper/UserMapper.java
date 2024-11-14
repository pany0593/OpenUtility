package com.group6.mapper;

import com.group6.pojo.User;

public interface UserMapper {
    /**
     *  插入新用户
     *
     * @param user
     */
    void insertUser(User user);

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     *  根据用户ID查找用户
     *
     * @param userId
     * @return
     */
    User findUserById(Long userId);

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatarPath
     */
    void updateUserAvatar(Long userId, String avatarPath);
}
