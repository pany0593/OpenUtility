package com.group6.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String commentId;
    private String fatherId;
    private String userId;
    private String userName;
    private String content;
    private String createTime;
    private int likes = 0;
    private int level;
    //若文章评论从未被查看过，就不会生成subComments列表
    //在第一次被查看后生成已存在的本文文章评论的subComments
    private List<Comment> subComments = null;
}