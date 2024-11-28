package com.group6.mapper;

import com.group6.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommentMapper {//Comment表

    @Insert("insert into Comment (commentId , fatherId , userId , content , createTime , level) " +
            "values (#{commentId} , #{fatherId} , #{userId} , #{content} , CURRENT_TIMESTAMP, #{level})")
    void addComment(String commentId, String fatherId, String userId, String content, Integer level);

    @Select("select commentId,fatherId,userId,content,createTime,likes,level,avatarUrl from Comment where commentId = #{commentId} ")
    Comment findByCommentId(String commentId);

    @Delete("DELETE FROM Comment WHERE commentId = #{commentId} and level = 2")
    int delete_level2_Comment(String commentId);//删除二级评论本身

    @Delete("DELETE FROM Comment WHERE fatherId = #{commentId} and level = 2")
    int delete_son_Comment(String commentId);//删除一级评论的子评论

    @Delete("DELETE FROM Comment WHERE commentId = #{commentId} and level = 1")
    int delete_level1_Comment(String commentId);//删除一级评论本身

    @Update("update Comment set likes = likes + 1 where commentId = #{commentId}")
    void likeComment(String commentId);

    @Select("select commentId,fatherId,userId,content,createTime,likes,level from Comment where fatherId = #{fatherId} and level = 1")
    List<Comment> findFirstByArticleId(String fatherId);//找到fatherId是articleId的评论，即一级评论列表

    @Select("select commentId,fatherId,userId,content,createTime,likes,level from Comment where fatherId = #{commentId} and level = 2")
    List<Comment> findSecondByCommentId(String commentId);//将一级评论的commentId当作fatherId传入，寻找它的二级评论列表
}
