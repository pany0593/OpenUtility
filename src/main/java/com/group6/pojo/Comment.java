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
    private List<Comment> subComments;
}