package com.group6.mapper;

import com.group6.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface CommentMapper {//Comment表

    @Insert("INSERT INTO comment (commentId , fatherId , userId ,userName, content , createTime , level) " +
            "values (#{comment.commentId} , #{comment.fatherId} , #{comment.userId} ,#{comment.userName}, #{comment.content} , CURRENT_TIMESTAMP, 1)")
    void add_level1_Comment(@Param("comment") Comment comment);

    @Insert("INSERT INTO comment (commentId , fatherId , userId , userName, content , createTime , level) " +
            "values (#{comment.commentId} , #{comment.fatherId} , #{comment.userId} , #{comment.userName}, #{comment.content} , CURRENT_TIMESTAMP, 2)")
    void add_level2_Comment(@Param("comment") Comment comment);

    @Select("select commentId,fatherId,userId,userName,content,createTime,likes,level from comment where commentId = #{commentId} ")
    Comment findByCommentId(String commentId);

    @Delete("DELETE FROM comment WHERE commentId = #{commentId} and level = 2")
    int delete_level2_Comment(@Param("commentId")String commentId);//删除二级评论本身

    @Delete("DELETE FROM comment WHERE fatherId = #{commentId} and level = 2")
    int delete_son_Comment(@Param("commentId")String commentId);//删除一级评论的子评论

    @Delete("DELETE FROM comment WHERE commentId = #{commentId} and level = 1")
    int delete_level1_Comment(@Param("commentId")String commentId);//删除一级评论本身

    @Update("update comment set likes = likes + 1 where commentId = #{commentId}")
    void likeComment(String commentId);

    @Select("select commentId,userId,fatherId,userName,content,createTime,likes,level from comment where fatherId = #{articleId} and level = 1")
    List<Comment> findFirstByArticleId(@Param("articleId")String articleId);//找到fatherId是articleId的评论，即一级评论列表

    @Select("select commentId,fatherId,userId,userName,content,createTime,likes,level from comment where fatherId = #{commentId} and level = 2")
    List<Comment> findSecondByCommentId(@Param("commentId")String commentId);//将一级评论的commentId当作fatherId传入，寻找它的二级评论列表

    @Select("select commentId from comment ")
    List<Comment> findAllComment();

    @Select("select commentId from comment where fatherId = #{fatherId}")
    List<Comment> findByfatherId(String fatherId);
}
