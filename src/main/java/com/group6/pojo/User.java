package com.group6.pojo;

public class User {
    private Long id;  // 用户ID
    private String username;  // 用户名
    private String password;  // 密码
    private String email;  // 邮箱
    private String avatar;  // 用户头像路径

    /**
     * 无参构造函数
     *
     */
    public User() {
    }

    /**
     * 全参构造函数
     *
     * @param id
     * @param username
     * @param password
     * @param email
     * @param avatar
     */
    public User(Long id, String username, String password, String email, String avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
    }

    /**
     *Getter 和 Setter 方法
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

}
