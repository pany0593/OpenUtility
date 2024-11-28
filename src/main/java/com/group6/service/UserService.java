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
    public void registerUser(User user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }
        // 使用 UUID 的部分哈希值生成 Long 类型的用户 ID
        UUID uuid = UUID.randomUUID();
        long uniqueId = uuid.getMostSignificantBits() & Long.MAX_VALUE;
        user.setId(uniqueId);
        //加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //添加默认头像
        if (user.getAvatarPath() == null || user.getAvatarPath().isEmpty()) {
            user.setAvatarPath(generateDefaultAvatar());
        }
        saveUserToDatabase(user);
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
     * @param userId
     * @return
     */
    public User getUserById(Long userId) {
        return findUserById(userId);
    }

    /**
     * 更新用户头像
     *
     * @param userId
     * @param avatar
     */
    public void updateUserAvatar(Long userId, String avatar) {
        User user = findUserById(userId);
        if (user != null) {
            user.setAvatarPath(avatar);
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
        return "https://www.bing.com/images/search?view=detailV2&ccid=lb3OzXaN&id=E6F375AFBF9A0D0650205779FC0AF42D31DDDA50&thid=OIP.lb3OzXaNmiUsTlJMSddldAHaHa&mediaurl=https%3A%2F%2Fp3-pc-sign.douyinpic.com%2Ftos-cn-i-0813%2FoAYAQf7vtgN9DeAQAToEABCCYAFFIVlnnTD4AA%7Enoop.jpeg%3Fbiz_tag%3Dpcweb_cover%26from%3D327834062%26s%3DPackSourceEnum_SEARCH%26se%3Dfalse%26x-expires%3D1731229200%26x-signature%3D3xU62jASl7qEgAW1UHBprBxT1E4%253D&exph=1440&expw=1440&q=%E9%A5%BA%E5%AD%90%E6%81%B6%E6%90%9E%E4%B9%8B%E5%AE%B6%E5%A4%B4%E5%83%8F&FORM=IRPRST&ck=36105F80E6B207A6F8D3BD34F16A1C19&selectedIndex=89&itb=0&cw=1397&ch=647&ajaxhist=0&ajaxserp=0";
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
     * @param userId
     * @return
     */
    private User findUserById(Long userId) {
        return userMapper.findUserById(userId);
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
        userMapper.updateUserAvatar(user.getId(), user.getAvatarPath());
    }
}
