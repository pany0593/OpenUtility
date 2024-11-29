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
    private String title;
    private String authorId;
    private String authorName;
    private String desc;
    private String content;
    private String createTime;
    private int likes = 0;
    private int clicks = 0;

//    public Article(String title, String authorId, String authorName, String desc, String content) {
//        this.title = title;
//        this.authorId = authorId;
//        this.authorName = authorName;
//        this.desc = desc;
//        this.content = content;
//    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId='" + articleId + '\'' +
                ", title='" + title + '\'' +
                ", authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", desc='" + desc + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", likes=" + likes +
                ", clicks=" + clicks +
                '}';
    }
}