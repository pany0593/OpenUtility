package com.group6.service;

import com.group6.mapper.UserMapper;
import com.group6.pojo.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
    //生成JWT密钥
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     *  注册用户
     *
     * @param user
     */
//    public void registerUser(User user) {
//        if (userExists(user.getUsername())) {
//            throw new IllegalArgumentException("User already exists");
//        }
//        // 使用 UUID 生成 String 类型的用户 ID
//        UUID uuid = UUID.randomUUID();
//        String uniqueId = uuid.toString();
//        user.setId(uniqueId);
//        //加密密码
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        //添加默认头像
//        if (user.getAvatarPath() == null || user.getAvatarPath().isEmpty()) {
//            user.setAvatarPath(generateDefaultAvatar());
//        }
//        saveUserToDatabase(user);
//    }
    public void registerUser(User user) {
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

        try {
            saveUserToDatabase(user);
        } catch (PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                throw new IllegalArgumentException("User already exists or data integrity violation");
            }
            if (cause != null) {
                throw new IllegalArgumentException("Persistence error: " + cause.getMessage());
            }
            throw new IllegalArgumentException("Persistence error occurred, but cause is null");
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error occurred: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
        }
    }

    /**
     * 用户登录，返回 JWT 令牌
     *
     * @param username
     * @param password
     * @return
     */
    public String loginUser(String username, String password) {
        if (authenticateUser(username, password)) {
            // 生成 JWT 令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 令牌有效期一天
                    .signWith(SECRET_KEY)
                    .compact();
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
    private User findUserByUsername(String username) {
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



}
