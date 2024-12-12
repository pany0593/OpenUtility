package com.group6.controller;

import com.group6.pojo.Result;
import com.group6.util.UserProfileUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.group6.pojo.User;
import com.group6.service.UserService;

import java.util.List;
import java.util.Map;

import com.group6.util.JwtUtils;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Result registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return Result.success(user);
        } catch (IllegalArgumentException e) {
            // 捕获非法参数异常，并返回错误信息
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他未预见的异常，并返回通用错误信息
            return Result.error("Unexpected error occurred: " + e.getMessage());
        }
    }



    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result loginUser(@RequestBody User user) {
        try {
            String token = userService.loginUser(user.getUsername(), user.getPassword(),user.getBuilding(),user.getDormitory());
            return Result.success(token);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 获取当前用户信息接口
     * 允许 `ADMIN` 和 `USER` 角色访问
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Result getUserProfile() {
        try {
            // 调用工具类方法获取当前用户信息
            User user = UserProfileUtil.getUserProfile();
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 获取所有用户（仅限管理员权限）
     */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    /**
     * 根据宿舍楼号和房间号查询用户（仅限管理员权限）
     */
    @GetMapping("/by-room")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result getUsersByBuildingAndRoom(
            @RequestParam String building,
            @RequestParam String dormitory
    ) {
        List<User> users = userService.getUsersByBuildingAndRoom(building, dormitory);
        if (users.isEmpty()) {
            return Result.error("No users found for the specified building and dormitory.");
        }
        return Result.success(users);
    }

    /**
     * 删除用户（仅限管理员权限）
     */
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            return Result.success("User deleted successfully.");
        }
        return Result.error("Failed to delete user. User may not exist.");
    }

}

