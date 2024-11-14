package com.group6.service;

import com.group6.pojo.User;

public class UserService {

    /**
     *  注册用户
     *
     * @param user
     */
    public void registerUser(User user) {

    }

    /**
     * 用户登录，返回 JWT 令牌
     *
     * @param username
     * @param password
     * @return
     */
    public String loginUser(String username, String password) {

        return null;
    }

    /**
     * 根据用户 ID 获取用户信息
     *
     * @param userId
     * @return
     */
    public User getUserById(Long userId) {

        return null;
    }

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatarPath
     */
    public void updateUserAvatar(Long userId, String avatarPath) {

    }

    /**
     * 生成默认头像
     *
     * @return
     */
    public String generateDefaultAvatar() {

        return null;
    }

}
