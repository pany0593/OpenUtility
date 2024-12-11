package com.group6.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity

public class User {

//    @Id
    private String id;  // 用户ID

    private String username;  // 用户名
    private String password;  // 密码

//    @Column(nullable = true)
    private String email;  // 邮箱
//    @Column(name = "avatar_path", length = 1024)
    private String avatar;  // 用户头像路径
    private String role;
    private Integer building;
    private Integer dormitory;
}
