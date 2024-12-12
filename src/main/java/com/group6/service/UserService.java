package com.group6.service;

import com.group6.mapper.UserMapper;
import com.group6.pojo.User;
import com.group6.util.JwtUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.apache.ibatis.exceptions.PersistenceException;

@Service
public class UserService {

    // 注入 UserMapper 实例
    @Autowired
    private UserMapper userMapper;
    // 注入 BCryptPasswordEncoder 实例
    @Autowired
    private PasswordEncoder passwordEncoder;
//    //生成JWT密钥
//    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    public String registerUser(User user) {
        // 校验 user 对象及必要字段
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (user.getBuilding() == null || user.getDormitory() == null) {
            throw new IllegalArgumentException("Building and dormitory cannot be null");
        }

        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        // 设置默认值
        user.setId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 30));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getEmail() == null) {
            user.setEmail("default@example.com");
        }
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar(generateDefaultAvatar());
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        // 保存用户到数据库
        try {
            saveUserToDatabase(user);
        } catch (PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                throw new IllegalArgumentException("User already exists or data integrity violation");
            }
            throw new IllegalArgumentException("Persistence error: " + (cause != null ? cause.getMessage() : "Unknown cause"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error occurred: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
        }

        // 注册成功后生成 JWT 并返回
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        claims.put("building", user.getBuilding()); // 添加楼号
        claims.put("dormitory", user.getDormitory()); // 添加宿舍号
        return JwtUtils.generateToken(claims, user.getId()); // 使用 userId 作为 JWT 的 subject
    }

    /**
     * 用户登录，返回 JWT 令牌
     *
     * @param username   用户名
     * @param password   密码
     * @param building   楼号
     * @param dormitory  宿舍号
     * @return JWT 令牌
     */
    public String loginUser(String username, String password, Integer building, Integer dormitory) {
        // 验证用户名和密码
        if (authenticateUser(username, password)) {
            User user = findUserByUsername(username); // 获取完整用户信息

            // 校验楼号和宿舍号是否匹配
            if (!building.equals(user.getBuilding()) || !dormitory.equals(user.getDormitory())) {
                throw new IllegalArgumentException("Building or dormitory does not match");
            }

            // 生成 JWT 的 claims
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("role", user.getRole());
            claims.put("building", building);
            claims.put("dormitory", dormitory);

            // 调用 JwtUtils 生成 JWT
            String jwtToken = JwtUtils.generateToken(claims, user.getId()); // 使用现有逻辑生成 JWT

            // 解析 JWT 并生成 Authentication
            String usernameFromClaims = (String) claims.get("username");
            String roleFromClaims = (String) claims.get("role");
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleFromClaims));

            // 创建并设置 Authentication 到 SecurityContext
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usernameFromClaims, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 返回生成的 JWT
            return jwtToken;
        }

        throw new IllegalArgumentException("Invalid username or password");
    }


    /**
     * 根据用户 ID 获取用户信息
     *
     * @param id
     * @return
     */
    public User getUserById(String id) {
        return findUserById(id);
    }

    /**
     * 更新用户头像
     *
     * @param id
     * @param avatar
     */
    public void updateUserAvatar(String id, String avatar) {
        User user = findUserById(id);
        if (user != null) {
            user.setAvatar(avatar);
            updateUserInDatabase(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }

    }

    /**
     * 生成默认头像
     *
     * @return
     */
    public String generateDefaultAvatar() {
        return "https://th.bing.com/th/id/OIP.lb3OzXaNmiUsTlJMSddldAHaHa?rs=1&pid=ImgDetMain";
    }
    private boolean authenticateUser(String username, String password) {
        User user = findUserByUsername(username);
        // 使用 PasswordEncoder 校验密码
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }


    /**
     * 检查用户是否已经存在
     *
     * @param username
     * @return
     */
    private boolean userExists(String username) {
        return findUserByUsername(username) != null;
    }

    /**
     * 将用户信息保存到数据库
     *
     * @param user
     */
    private void saveUserToDatabase(User user) {
        userMapper.insertUser(user);
    }

    /**
     * 根据用户 ID 查找用户
     *
     * @param id
     * @return
     */
    private User findUserById(String id) {
        return userMapper.findUserById(id);
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    /**
     * 更新用户信息到数据库
     *
     * @param user
     */
    private void updateUserInDatabase(User user) {
        userMapper.updateUserAvatar(user.getId(), user.getAvatar());
    }


    public boolean adminExists() {
        int count = userMapper.findAdminCount();
        System.out.println("Admin count: " + count); // 日志验证
        return count > 0;
    }



    // 仅管理员可以调用
//    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userMapper.findAllNonAdminUsers();
    }

    /**
     * 根据楼号和宿舍号查询非管理员用户
     * @param building 楼号
     * @param dormitory 宿舍号
     * @return 非管理员用户列表
     */
//    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getUsersByBuildingAndRoom(String building, String dormitory) {
        return userMapper.findUsersByBuildingAndRoom(building, dormitory);
    }

    public boolean deleteUserById(String id) {
        int rowsAffected = userMapper.deleteUserById(id);
        return rowsAffected > 0;
    }
}
