package com.group6.config;

import com.group6.pojo.User;
import com.group6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountInitializer {

    @Autowired
    private UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAdminAccount() {
        // 检查是否存在管理员账号
        if (!userService.adminExists()) {

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("password"); // 明文密码，将被加密
            admin.setBuilding(0);
            admin.setDormitory(0);
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");

            // 注册默认管理员账号
            userService.registerUser(admin);
            System.out.println("Default admin account created.");
        } else {
            System.out.println("Admin account already exists.");
        }
    }
}
