package com.group6.util;

import com.group6.pojo.User;
import com.group6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserProfileUtil {

    private static UserService userService;

    // 使用 Spring 自动注入 UserService
    @Autowired
    public void setUserService(UserService userService) {
        UserProfileUtil.userService = userService;
    }

    public static User getUserProfile() {
        // 从 SecurityContextHolder 获取当前用户的 Authentication 对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // 从 Authentication 获取当前用户的 principal 对象
            Object principal = authentication.getPrincipal();

            // 如果 principal 是 User 类型，直接返回用户对象
            if (principal instanceof User user) {
                return user;
            }
            // 如果 principal 是 String 类型（可能是用户 ID），则从数据库获取用户信息
            if (principal instanceof String userId) {
                User user = userService.getUserById(userId); // 调用 UserService 获取用户
                return user;
            }
        }

        // 如果未认证或未找到用户，抛出异常
        throw new IllegalStateException("User not authenticated or not found.");
    }
}
