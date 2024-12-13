package com.group6.controller;

import com.group6.pojo.*;
import org.springframework.web.bind.annotation.PostMapping;
import com.group6.service.PostService;
import com.group6.response.GetArticleListResponse;
import com.group6.response.GetNoticeListResponse;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")

public class PostController {

    @Autowired
    private PostService postService;


    private Integer pagesPerPage =4;//设置文章列表为每页4篇文章

    //发帖(done)
    @PostMapping("/article_add")//done
    public Result article_add(@RequestBody Article article) {
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

    //发公告
    @PostMapping("/notice_add")//done
    public Result notice_add(@RequestBody Article article) {
        try {
            article.setArticleId(postService.createNoticeId());
            if(postService.createNotice(article) != 0){
                return Result.success(article.getArticleId());
            } else {
                return Result.error("fail to add notice");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //删帖(done)
    @DeleteMapping("/article_delete")//done
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

    //删公告(done)
    @DeleteMapping("/notice_delete")//done
    public Result notice_delete(@RequestBody Article article) {
        try {
            if (postService.deleteNotice(article)) {
                return Result.success();
            } else {
                return Result.error("Failed to delete notice");
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

    //更新公告(done)
    @PostMapping("/notice_update")//done
    public Result notice_update(@RequestBody Article article) {
        try {
            postService.updateNotice(article);
            return Result.success();
        }
        catch (Exception e) {
            return Result.error("Failed to update notice");
        }
    }


    //点赞文章//service层工具类要更换
    @PostMapping("/article_like")
    public Result likeArticle(@RequestBody Article article0) {
        if (article0.getArticleId() == null || article0.getArticleId().isEmpty()) {
            return Result.error("Failed to find article : empty articleId");
        }
        Article article = postService.findByArticleId(article0.getArticleId());
        if (article == null) {//根据文章id获取文章对象失败
            return Result.error("Failed to find article : articleId do not exist");
        }
        List<Article> likesArticle = postService.getLikesByUserId();//获取该用户的点赞列表
        for (Article tmpArticle : likesArticle) {
            if (tmpArticle.getArticleId().equals(article0.getArticleId())) {
                return Result.error("Fail to like article : already liked");
            }
        }
        postService.likeArticle(article0.getArticleId());
        return Result.success();
    }

    //发表评论(done)
    @PostMapping("/comment_add")//done
    public Result comment_add(@RequestBody Comment comment) {
        try{
            String commentId = postService.addComment(comment);
            if (commentId.equals("Invalid")) {//错误的在公告上添加评论
                throw new Exception();
            }
            return Result.success(postService.findByCommentId(commentId));
        } catch (Exception e) {
            return Result.error("Failed to add comment");
        }
    }

    //删除评论(done)
    @DeleteMapping("/comment_delete")
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

    //点赞评论(未完成)
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
    @PostMapping("/article_get")
    public Result getArticle(@RequestBody Article article0) {
        Article article = null;
        try {
            article = postService.findByArticleId(article0.getArticleId());
            postService.addClicks(article.getArticleId());
        } catch (Exception e) {
            return Result.error("fail to find article");
        }
        return Result.success(article);
    }

    //查看评论(done)
    @PostMapping("/comment_get")
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
    @PostMapping("/article_list_get")
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

    //查看公告列表
    @PostMapping("/notice_list_get")
    public Result getList_notice(@RequestBody ArticleList articlelist) {
        try {
            //获取公告数量
            int noticeNum=postService.getNoticeNum();
            //公告页数大于或小于极限
            if (articlelist.getPage() < 1 || articlelist.getPage() > (int) Math.ceil((double) noticeNum / pagesPerPage)) {
                return Result.error("page error");
            }
            //生成公告列表
            List<Article> notices = postService.getList_notice(articlelist, pagesPerPage);
            //判断公告列表非空
            if (notices == null || notices.isEmpty()) {
                return Result.error("list is empty");
            }

            return Result.success(new GetNoticeListResponse(notices, (int) Math.ceil((double) noticeNum / pagesPerPage)));
        } catch (Exception e){
            return Result.error("fail to get notice list");
        }
    }

}


