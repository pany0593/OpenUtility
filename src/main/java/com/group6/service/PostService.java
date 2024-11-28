package com.group6.service;

import com.group6.pojo.Article;
import com.group6.pojo.Comment;
import com.group6.mapper.ArticleMapper;
import com.group6.mapper.CommentMapper;
import com.group6.util.ThreadLocalUtil;
import com.group6.mapper.FavoriteMapper;
import com.group6.pojo.Favorite;
import com.group6.util.SnowFlakeUtils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PostService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private ThreadLocalUtil threadLocalUtil;
    @Autowired
    private SnowFlakeUtils snowFlakeUtils;
    @Autowired
    private UserService userService;

    //生成文章id
    public String createArticleId() {
        return "AR"+ snowFlakeUtils.nextId();
    }

    //创建文章
    public boolean createArticle(String articleId, String title, String content, String desc) {
        String authorId= (String) threadLocalUtil.get("userId");
        return articleMapper.insertArticle(articleId,title,content,authorId,desc) > 0;
    }

    //删除文章
    public boolean deleteArticle(String articleId) {
        return articleMapper.deleteArticle(articleId) > 0;
    }

    //更新文章
    public boolean updateArticle(Article article) {
        return articleMapper.updateArticle(article) > 0;
    }


    //发表评论
    public String addComment(String articleId, String content, int level) {
        String userId = (String) threadLocalUtil.get("userId");
        String commentId="CM"+snowFlakeUtils.nextId();
        commentMapper.addComment(commentId,articleId,userId,content,level);
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
        if (commentMapper.delete_son_Comment(commentId) > 0) {
            return commentMapper.delete_level1_Comment(commentId) > 0;
        }
        return false;
    }

    //查看文章评论
    public List<Comment> getCommentByArticleId(String articleId) {
        List<Comment> comments = commentMapper.findFirstByArticleId(articleId);
        for (Comment comment:comments) {
            comment.setSubComments(commentMapper.findSecondByCommentId(comment.getCommentId()));
//            comment.setUserName(userService.getNameById(comment.getUserId()));        //由UserService实现
        }
        return comments;
    }


    public List<Article> getList(Integer page, Integer sort,Integer pagesPerPage) {
        List<Article> articles;
        if(sort==1){
            articles=articleMapper.getListByTime((page - 1) * pagesPerPage, pagesPerPage);
        }else{
            articles=articleMapper.getListByClick((page - 1) * pagesPerPage, pagesPerPage);
        }
/*        for (Article article:articles) {
            article.setAuthorName(userService.getNameById(article.getAuthorId()));      //由UserService实现
        }
*/
        return articles;
    }

    public int getArticleNum() {
        return articleMapper.getArticleNum();
    }

    public List<Comment> getFavoriteCommentByUserId() {
        return favoriteMapper.getFavoriteCommentByUserId(String.valueOf(threadLocalUtil.get("userId")));
    }

    public Article findByArticleId(String articleId) {
        Article article = articleMapper.findByArticleId(articleId);
        return article;
    }

    public List<Article> getLikesByUserId() {
        String userId = String.valueOf(threadLocalUtil.get("userId"));
        List<Favorite> favorites = favoriteMapper.getFavoriteArticleByUserId(userId);
        List<Article> articles = new ArrayList<>();
        for (Favorite favorite : favorites) {
            articles.add(articleMapper.findByArticleId(favorite.getArticleId()));
        }
        return articles;
    }

    public void addClicks(String articleId) {
        articleMapper.addClicks(articleId);
    }

    public void likeArticle(String articleId) {
        articleMapper.likeArticle(articleId);
        favoriteMapper.addFavoriteArticle(String.valueOf(threadLocalUtil.get("userId")),articleId);
    }

    public void likeComment(String commentId) {
        commentMapper.likeComment(commentId);
        favoriteMapper.addFavoriteComment(String.valueOf(threadLocalUtil.get("userId")),commentId);
    }

/*    public String getNameById(String authorId) {
        return userService.getNameById(authorId);                                       //由UserService实现
    }
*/
}


