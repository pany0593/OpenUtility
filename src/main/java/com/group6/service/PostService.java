package com.group6.service;

import com.group6.pojo.Article;
import com.group6.pojo.Comment;
import com.group6.pojo.Result;
import java.util.List;

public class PostService {

    public String createArticleId() {}

    public void addArticle(String articleId, String title, String desc) {};

    public Article getArticle(String articleId) {};

    public List<Article> getArticleList(int page, int sort) {};

    public void likeArticle(String articleId) {};

    public String addComment(String articleId, String content, int level) {};

    public Comment getComments(String articleId) {};

    public void  likeComment(String commentId) {};
}
