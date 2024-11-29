package com.group6.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.group6.pojo.Result;
import com.group6.pojo.Article;
import com.group6.pojo.Comment;
import com.group6.service.PostService;
import com.group6.response.GetArticleListResponse;

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
    public Result article_add(@RequestBody Article article) {
        System.out.println(article.getDesc());
        try {
            article.setArticleId(postService.createArticleId());
            if(postService.createArticle(article) != 0){
                return Result.success(article.getArticleId());
            } else {
                return Result.error("fail to add article");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //删帖(done)
    @PostMapping("/article_delete")//done
    public Result article_delete(@RequestBody Article article) {

        try {
            if (postService.deleteArticle(article)) {
                return Result.success();
            } else {
                return Result.error("Failed to delete article");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //更新帖子(done)
    @PostMapping("/article_update")//done
    public Result article_update(@RequestBody Article article) {
        try {
            if (postService.updateArticle(article)) {
                return Result.success();
            } else {
                return Result.error("Failed to update article");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //点赞文章(done)
    @PostMapping("/article_like")
    public Result likeArticle(@RequestBody Article article0) {
        Article article = postService.findByArticleId(article0.getArticleId());
        if (article == null) {
            return Result.error("Failed to find article");
        }
        List<Article> likesArticle = postService.getLikesByUserId();//service层未实现//已实现
        for (Article tmpArticle : likesArticle) {
            if (tmpArticle.getArticleId().equals(article0.getArticleId())) {
                return Result.error("already liked");
            }
        }
        postService.likeArticle(article0.getArticleId());
        return Result.success();
    }

    //发表评论(done)
    @PostMapping("/comment_add")//done
    public Result comment_add(@RequestBody Comment comment) {
        String commentId = postService.addComment(comment);
        return Result.success(postService.findByCommentId(commentId));
    }

    //删除评论(done)
    @PostMapping("/comment_delete")
    public Result comment_delete(@RequestBody Comment comment0) {
        Comment comment = postService.findByCommentId(comment0.getCommentId());
        if (comment.getLevel() == 2) {
            if (postService.delete_level2_Comment(comment0.getCommentId())) {
                return Result.success();
            }
        } else {
            if (postService.delete_level1_Comment(comment0.getCommentId())) {
                return Result.success();
            }
        }
        return Result.error("Failed to delete comment");
    }

    //点赞评论(done)
    @PostMapping("/comment_like")
    private Result likeComment(@RequestBody Comment comment0){
        Comment comment=postService.findByCommentId(comment0.getCommentId());
        if(comment==null){
            return Result.error("fail to find comment");
        }
        List<Comment> favoriteComments = postService.getFavoriteCommentByUserId();
        for (Comment tmpComment:favoriteComments) {
            if(tmpComment.getCommentId().equals(comment0.getCommentId())){
                return Result.error("already liked");
            }
        }
        postService.likeComment(comment0.getCommentId());//点赞
        return Result.success();
    }


    //查看文章(done)
    @GetMapping("/article_get")
    public Result getArticle(@RequestBody Article article0) {
        Article article = null;
        try {
            article = postService.findByArticleId(article0.getArticleId());
//      postService.addClicks(article.getArticleId());
        } catch (Exception e) {
            return Result.error("fail to find article");
        }
        return Result.success(article);//直接返回文章内容,不需要Response?
    }

    //查看评论(done)
    @GetMapping("/comment_get")
    private Result getComment(@RequestBody Article article){
        List<Comment> comments = postService.getCommentByArticleId(article.getArticleId());
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
