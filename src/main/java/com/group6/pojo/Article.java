package com.group6.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private String articleId;
    private String noticeId;
    private String title;
    private String authorId;
    private String authorName;
    private String desc;
    private String content;
    private String createTime;
    private int likes = 0;
    private int clicks = 0;

}