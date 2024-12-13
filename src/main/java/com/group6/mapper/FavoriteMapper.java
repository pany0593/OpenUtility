package com.group6.mapper;

import com.group6.pojo.Comment;
import com.group6.pojo.FavoriteArticle;
import com.group6.pojo.FavoriteComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface FavoriteMapper {//用户的favorite_article表和favorite_comment表
    @Select("select userId,articleId from favorite_article where userId = #{userId}")
    List<FavoriteArticle> getFavoriteArticleByUserId(@Param("userId")String userId);//查询用户点赞过的文章

    @Select("select userId,commentId from favorite_comment where userId = #{userId}")
    List<FavoriteComment> getFavoriteCommentByUserId(@Param("userId")String userId);//查询用户点赞过的评论

    @Insert("insert into favorite_comment (userId,commentId) values (#{userId},#{commentId});")
    void addFavoriteComment(@Param("userId")String userId, @Param("commentId")String commentId);//点赞评论

    @Insert("INSERT into favorite_article (userId, articleId) values (#{userId}, #{articleId});")
    void addFavoriteArticle(String userId, String articleId);//点赞文章

}
