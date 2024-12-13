package com.group6.service;

import com.group6.pojo.*;
import com.group6.mapper.ArticleMapper;
import com.group6.mapper.CommentMapper;
import com.group6.mapper.FavoriteMapper;
import com.group6.util.SnowFlakeUtils;
import com.group6.util.UserProfileUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

@Component
public class PostService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private SnowFlakeUtils snowFlakeUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private View error;

    //生成文章id
    public String createArticleId() {
        return "AR"+ snowFlakeUtils.nextId();
    }

    //生成公告id
    public String createNoticeId() {
        return "NO"+snowFlakeUtils.nextId();
    }

    //创建文章
    public int createArticle(Article article) {
        try {
            return articleMapper.insertArticle(article);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //创建公告
    public int createNotice(Article article) {
        try {
            return articleMapper.insertNotice(article);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //删除文章
    public boolean deleteArticle(Article article) {
        return articleMapper.deleteArticle(article) > 0;
    }

    //删除公告
    public boolean deleteNotice(Article article) {
        return articleMapper.deleteNotice(article) > 0;
    }

    //更新文章
    public boolean updateArticle(Article article) {
        return articleMapper.updateArticle(article) > 0;
    }

    //更新公告
    public boolean updateNotice(Article article) {
        return articleMapper.updateNotice(article) > 0;
    }


    //发表评论
    public String addComment(Comment comment) {
        String commentId="CM"+snowFlakeUtils.nextId();
        comment.setCommentId(commentId);
        String CM = "CM";
        String AR = "AR";
        String NO = "NO";

        //比较fatherId是否是articleId来确认是一级还是二级评论
        if (comment.getFatherId() == null) {
            throw new IllegalArgumentException("FatherId is null");
        }
        try{
            if ((comment.getFatherId()).startsWith(CM)) {
//                System.out.println("get in 2");
                commentMapper.add_level2_Comment(comment);
            }else if ((comment.getFatherId()).startsWith(AR)){
//                System.out.println("get in 1");
                commentMapper.add_level1_Comment(comment);
            } else if ((comment.getFatherId()).startsWith(NO)){
                return "Invalid";
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("fail to compare");
        }
        return commentId;
    }

    //根据评论id搜索评论
    public Comment findByCommentId(String commentId) {
        return commentMapper.findByCommentId(commentId);
    }

    //删除二级评论
    public boolean delete_level2_Comment(String commentId) {
        return commentMapper.delete_level2_Comment(commentId) > 0;
    }

    //删除一级评论以及其二级评论
    public boolean delete_level1_Comment(String commentId) {
        //查看是否有子评论
        //遍历评论列表找已该评论为父评论的评论列表
        //若为空，则没有子评论，直接删除
        //若非空，则先删除子评论再删除自身
        List<Comment> soncomments = findByfatherId(commentId);
        if (soncomments == null || soncomments.isEmpty()) {
            return commentMapper.delete_level1_Comment(commentId) > 0;
        } else if (commentMapper.delete_son_Comment(commentId) > 0){
            return commentMapper.delete_level1_Comment(commentId) > 0;
        }
        return false;
    }

    //搜索特定fatherId的评论列表
    public List<Comment> findByfatherId(String fatherId) {
        List<Comment> soncomments = commentMapper.findByfatherId(fatherId);
        return soncomments;
    }

    //搜索所有评论
    public List<Comment> findAllByCommentId() {
        List<Comment> allcomments = commentMapper.findAllComment();
        return allcomments;
    }

    //查看文章评论
    public List<Comment> getCommentByArticleId(String articleId) {
        List<Comment> comments = commentMapper.findFirstByArticleId(articleId);//生成一级评论列表
        for (Comment tmpcomment:comments) {//循环生成每个二级评论列表
            tmpcomment.setSubComments(commentMapper.findSecondByCommentId(tmpcomment.getCommentId()));
            tmpcomment.subComments.sort(Comparator.comparing(Comment::getLikes).reversed());
        }
        return comments;
    }

    //文章列表
    public List<Article> getList(ArticleList articlelist, Integer pagesPerPage) {
        List<Article> articles;
        if(articlelist.getSort() == 1){//sort = 1是根据文章发表时间倒序排列，其他值是根据点击量倒序排序。
            articles=articleMapper.getListByTime((articlelist.getPage() - 1) * pagesPerPage, pagesPerPage);
        }else{
            articles=articleMapper.getListByClick((articlelist.getPage() - 1) * pagesPerPage, pagesPerPage);
        }
        return articles;
    }

    //公告列表
    public List<Article> getList_notice(ArticleList articlelist, Integer pagesPerPage) {
        List<Article> notices;
        notices = articleMapper.getListByTime_notice((articlelist.getPage() - 1) * pagesPerPage, pagesPerPage);
        return notices;
    }

    public int getArticleNum() {
        return articleMapper.getArticleNum();
    }

    public int getNoticeNum() {
        return articleMapper.getNoticeNum();
    }

    public Article findByArticleId(String articleId) {
        Article article = articleMapper.findByArticleId(articleId);
        return article;
    }

    //获取用户点赞评论列表
    public List<Comment> getFavoriteCommentByUserId() {
        String userId = UserProfileUtil.getUserProfile().getId();
        List<FavoriteComment> favorites = favoriteMapper.getFavoriteCommentByUserId(userId);
        List<Comment> comments = new ArrayList<>();
        for (FavoriteComment favorite : favorites) {
            comments.add(commentMapper.findByCommentId(favorite.getCommentId()));
        }
        return comments;
    }

    //获取用户点赞文章列表
    public List<Article> getLikesByUserId() {
        System.out.println("1");
        String userId = UserProfileUtil.getUserProfile().getId();
        System.out.println("2");
        List<FavoriteArticle> favorites = favoriteMapper.getFavoriteArticleByUserId(userId);
        List<Article> articles = new ArrayList<>();
        for (FavoriteArticle favorite : favorites) {
            articles.add(articleMapper.findByArticleId(favorite.getArticleId()));
        }
        return articles;
    }

    public void addClicks(String articleId) {
        articleMapper.addClicks(articleId);
    }

    //点赞文章
    public void likeArticle(String articleId) {
        articleMapper.likeArticle(articleId);
        System.out.println("6");
        favoriteMapper.addFavoriteArticle(UserProfileUtil.getUserProfile().getId(),articleId);
        System.out.println("7");
    }

    public void likeComment(String commentId) {
        commentMapper.likeComment(commentId);
        favoriteMapper.addFavoriteComment(UserProfileUtil.getUserProfile().getId(),commentId);
    }

}


