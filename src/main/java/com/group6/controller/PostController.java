package com.group6.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.transform.Result;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("/article_add")
    public Result addarticle(String title, String content) {}

    @GetMapping("/article_get")
    public Result getarticle(String articleId) {}

    @GetMapping("/article_list_get")
    public Result getlist(int page, int sort) {}

    @PostMapping("/article_like")
    public Result likearticle(int articleId) {}

    @PostMapping("/comment_add")
    public Result addcomment(String articleId,
                             @Pattern(regexp = "^\\S{1,99}$")String content,
                             int level) {}

    @GetMapping("/comment_get")
    public Result getcomment(String articleId) {}

    @PostMapping("/comment_like")
    public Result likecomment(String articleId) {}

}
