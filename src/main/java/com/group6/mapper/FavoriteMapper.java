package com.group6.mapper;

import com.group6.pojo.Comment;
import com.group6.pojo.Favorite;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
public interface FavoriteMapper {//favorite_article表和favorite_comment表
    @Select("select userId,articleId from favorite_article where userId = #{userId}")
    List<Favorite> getFavoriteArticleByUserId(String userId);//查询用户点赞过的文章

    @Select("select userId,commentId from favorite_comment where userId = #{userId}")
    List<Comment> getFavoriteCommentByUserId(String userId);//查询用户点赞过的评论

    @Insert("insert into favorite_comment (userId,commentId) values (#{userId},#{commentId});")
    void addFavoriteComment(String userId, String commentId);//点赞评论

    @Insert("INSERT into favorite_article (userId, articleId) values (#{userId}, #{articleId});")
    void addFavoriteArticle(String userId, String articleId);//点赞文章

}
