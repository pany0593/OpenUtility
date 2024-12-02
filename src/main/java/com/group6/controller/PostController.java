package com.group6.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.group6.pojo.Result;
import com.group6.pojo.Article;
import com.group6.pojo.Comment;
import com.group6.pojo.ArticleList;
import com.group6.service.PostService;
import com.group6.response.GetArticleListResponse;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")

public class PostController {

    @Autowired
    private PostService postService;

    private Integer pagesPerPage =4;//先设置为4篇文章每页

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
            postService.updateArticle(article);
            return Result.success();
        }
        catch (Exception e) {
            return Result.error("Failed to update article");
        }
    }

    //点赞文章(done)//如何获取进行点赞操作的用户id？
    @PostMapping("/article_like")
    public Result likeArticle(@RequestBody Article article0) {
        Article article = postService.findByArticleId(article0.getArticleId());
        if (article == null) {
            return Result.error("Failed to find article");
        }
        List<Article> likesArticle = postService.getLikesByUserId();
        for (Article tmpArticle : likesArticle) {
            if (tmpArticle.getArticleId().equals(article0.getArticleId())) {
                return Result.error("already liked");
            }
        }
        postService.likeArticle(article0.getArticleId());
        return Result.success();
    }

    //发表评论(done)//未添加验证fatherId是否是已存在的id
    @PostMapping("/comment_add")//done
    public Result comment_add(@RequestBody Comment comment) {
        try{
            String commentId = postService.addComment(comment);
            return Result.success(postService.findByCommentId(commentId));
        } catch (Exception e) {
            return Result.error("Failed to add comment");
        }
    }

    //删除评论(done)
    @PostMapping("/comment_delete")
    public Result comment_delete(@RequestBody Comment comment) {
        if (comment.getCommentId() == null) return Result.error("commentId is null");

        //检查该评论Id是否存在
        List<Comment> comments = postService.findAllByCommentId();
        int exist = 0;
        for (Comment tmpcomment : comments) {
            if (comment.getCommentId().equals(tmpcomment.getCommentId())) {
                exist = 1;
                break;
            }
        }
        if (exist == 0) {
            return Result.error("comment not exist");
        }

        //根据收到的评论ID找到对应的评论
        Comment comment0 = postService.findByCommentId(comment.getCommentId());

        //按评论等级选择不同的删除操作
        if (comment0.getLevel() == 2) {//删除二级评论
            if (postService.delete_level2_Comment(comment0.getCommentId())) {
                return Result.success();
            }
        } else {//删除一级评论及其子评论
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


    //查看文章(done)//已修改
    @GetMapping("/article_get")
    public Result getArticle(@RequestBody Article article0) {
        Article article = null;
        try {
            article = postService.findByArticleId(article0.getArticleId());
            postService.addClicks(article.getArticleId());
        } catch (Exception e) {
            return Result.error("fail to find article");
        }
        return Result.success(article);//直接返回文章内容,不需要Response?
    }

    //查看评论(done)
    @GetMapping("/comment_get")
    private Result getComment(@RequestBody Article article){
        //点击评论区展开一级评论列表，此时生成所有一级评论的二级评论列表
        //评论列表由点赞数降序排序
        //展示作者名、评论内容、评论时间和点赞数
        //因为点击文章才可能展开评论区，所以不存在articleId不存在的情况，无需检测
        try{
            List<Comment> comments = postService.getCommentByArticleId(article.getArticleId());
            comments.sort(Comparator.comparing(Comment::getLikes).reversed());
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error("fail to find comments");
        }
    }

    //查看文章列表(done)
    @GetMapping("/article_list_get")
    public Result getList(@RequestBody ArticleList articlelist) {
        try {
            //获取文章数量
            int articleNum=postService.getArticleNum();

            //文章页数大于或小于极限
            if (articlelist.getPage() < 1 || articlelist.getPage() > (int) Math.ceil((double) articleNum / pagesPerPage)) {
                return Result.error("page error");
            }

            //生成文章列表
            List<Article> articles = postService.getList(articlelist, pagesPerPage);

            //判断文章列表非空
            if (articles == null || articles.isEmpty()) {
                return Result.error("list is empty");
            }

            return Result.success(new GetArticleListResponse(articles, (int) Math.ceil((double) articleNum / pagesPerPage)));
        } catch (Exception e){
            return Result.error("fail to get article list");
        }
    }
}
