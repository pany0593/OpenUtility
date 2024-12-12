package com.group6.util;

import com.group6.pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.group6.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ProfileUtil {

    @Autowired
    private UserService userService;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    public User get() {
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                // 从 Authentication 获取当前用户的 principal 对象
                Object principal = authentication.getPrincipal();

                // 如果 principal 是 User 类型，直接返回用户对象
                if (principal instanceof User) {
                    return (User)principal;
                }
                if (principal instanceof String userId) {
                    User user = userService.getUserById(userId); // 从数据库获取用户信息
                    return user;
                }
            } catch (Exception e) {
                System.out.println("User Not Found");
            }
        }
        throw new RuntimeException("User Not Found");
    }
}
