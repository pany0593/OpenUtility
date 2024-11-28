package com.group6.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.PipedReader;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String commentId;
    private String fatherId;
    private String userId;
    private String userName;
    private String comment_content;
    private String createTime;
    private Integer likes;
    private Integer level;
    private List<Comment> subComments;
}