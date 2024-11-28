package com.group6.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.group6.pojo.Result;
import com.group6.pojo.Article;
import com.group6.pojo.Comment;
import com.group6.service.PostService;
import com.group6.response.GetArticleListResponse;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")

public class PostController {

    @Autowired
    private PostService postService;
    private Integer pagesPerPage=4;

    //发帖(done)
    @PostMapping("/article_add")//done
    public Result article_add(String title, String content, String desc) {
        String articleId = postService.createArticleId();
        if (postService.createArticle(articleId, title, content, desc)){
            return Result.success();
        } else {
            return Result.error("Fail to add article");
        }
    }

    //删帖(done)
    @PostMapping("/article_delete")//done
    public Result article_delete(@RequestParam String id) {
        if (postService.deleteArticle(id)) {
            return Result.success();
        } else {
            return Result.error("Failed to delete article");
        }
    }

    //更新帖子(done)
    @PostMapping("/article_update")//done
    public Result article_update(@RequestBody Article article) {
        if (postService.updateArticle(article)) {
            return Result.success();
        } else {
            return Result.error("Failed to update article");
        }
    }

    //点赞文章(done)
    @PostMapping("/article_like")
    public Result likeArticle(String articleId) {
        Article article = postService.findByArticleId(articleId);
        if (article == null) {
            return Result.error("Failed to find article");
        }
        List<Article> likesArticle = postService.getLikesByUserId();//service层未实现//已实现
        for (Article tmpArticle : likesArticle) {
            if (tmpArticle.getArticleId().equals(articleId)) {
                return Result.error("already liked");
            }
        }
        postService.likeArticle(articleId);
        return Result.success();
    }

    //发表评论(done)
    @PostMapping("/comment_add")//done
    public Result comment_add(String fatherId, String content, int level) {
        String commentId = postService.addComment(fatherId, content, level);
        Comment comment=postService.findByCommentId(commentId);
        return Result.success(comment);
    }

    //删除评论(done)
    @PostMapping("/comment_delete")
    public Result comment_delete(@RequestParam String id) {
        Comment comment = postService.findByCommentId(id);
        if (comment.getLevel() == 2) {
            if (postService.delete_level2_Comment(id)) {
                return Result.success();
            }
        } else {
            if (postService.delete_level1_Comment(id)) {
                return Result.success();
            }
        }
        return Result.error("Failed to delete comment");
    }

    //点赞评论(done)
    @PostMapping("/comment_like")
    private Result likeComment(String commentId){
        Comment comment=postService.findByCommentId(commentId);
        if(comment==null){
            return Result.error("fail to find comment");
        }
        List<Comment> favoriteComments = postService.getFavoriteCommentByUserId();
        for (Comment tmpComment:favoriteComments) {
            if(tmpComment.getCommentId().equals(commentId)){
                return Result.error("already liked");
            }
        }
        postService.likeComment(commentId);
        return Result.success();
    }


    //查看文章(done)
    @GetMapping("/article_get")
    public Result getArticle(String articleId) {
        Article article = postService.findByArticleId(articleId);
        if (article == null) {
            return Result.error("fail to find article");
        }
        postService.addClicks(articleId);
        return Result.success(article.getContent());//直接返回文章内容,不需要Response?
    }

    //查看文章评论(done)
    @GetMapping("/comment_get")
    private Result getComment(String articleId){
        List<Comment> comments = postService.getCommentByArticleId(articleId);
        return Result.success(comments);
    }

    //查看文章列表(done)
    @GetMapping("/article_list_get")
    public Result getList(Integer page, Integer sort) {
        if (page < 1) {
            return Result.error("page error");
        }
        List<Article> articles = postService.getList(page, sort, pagesPerPage);
        if (articles == null || articles.isEmpty()) {
            return Result.error("list is empty");
        }
        int articleNum=postService.getArticleNum();
        return Result.success(new GetArticleListResponse(articles, (int) Math.ceil((double) articleNum /pagesPerPage)));
    }

}
