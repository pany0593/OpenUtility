package com.group6.mapper;

import com.group6.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {//Article表、notice表
    @Insert("INSERT INTO article (articleId, authorId, authorName, title, `desc`, content) " +
            "VALUES (#{article.articleId}, #{article.authorId}, #{article.authorName}, #{article.title}, #{article.desc}, #{article.content})")
    int insertArticle(@Param("article") Article article);//增加文章

    @Insert("INSERT INTO notice (noticeId, authorId, authorName, title, `desc`, content) " +
            "VALUES (#{notice.articleId}, #{notice.authorId}, #{notice.authorName}, #{notice.title}, #{notice.desc}, #{notice.content})")
    int insertNotice(@Param("notice") Article article);//增加公告

    @Delete("DELETE FROM article WHERE articleId = #{article.articleId}")
    int deleteArticle(@Param("article") Article article);//删除文章

    @Delete("DELETE FROM notice WHERE noticeId = #{notice.articleId}")
    int deleteNotice(@Param("notice") Article article);//删除公告

    @Update("UPDATE article SET title = #{article.title}, `desc` = #{article.desc}, content = #{article.content} WHERE articleId = #{article.articleId}")
    int updateArticle(@Param("article")Article article);//修改文章

    @Update("UPDATE notice SET title = #{notice.title}, `desc` = #{notice.desc}, content = #{notice.content} WHERE noticeId = #{notice.articleId}")
    int updateNotice(@Param("notice")Article article);//修改公告

    @Select("SELECT articleId,title,authorId,authorName,`desc`,content,createTime,likes,clicks from article where articleId = #{articleId}")
    Article findByArticleId(@Param("articleId") String articleId);//根据文章id返回文章具体内容

    @Select("SELECT noticeId,title,authorId,authorName,`desc`,content,createTime from notice where noticeId = #{noticeId}")
    Article findByNoticeId(@Param("noticeId") String noticeId);//根据公告id返回公告具体内容

    @Update("UPDATE article set likes = likes + 1 where articleId = #{articleId}")
    void likeArticle(@Param("articleId")String articleId);//点赞文章

    @Update("UPDATE article set clicks = clicks + 1 where articleId = #{articleId}")
    void addClicks(@Param("articleId") String articleId);//增加文章点击

    @Select("select articleId,title,authorId,authorName,`desc`,createTime,likes,clicks from article order by createTime desc limit #{offset}, #{pagesPerPage}")
    List<Article> getListByTime(@Param("offset")Integer offset, @Param("pagesPerPage")Integer pagesPerPage);//根据时间排序文章列表

    @Select("select articleId,title,authorId,authorName,`desc`,createTime,likes,clicks from article order by clicks desc limit #{offset}, #{pagesPerPage}")
    List<Article> getListByClick(@Param("offset")Integer offset,@Param("pagesPerPage")Integer pagesPerPage);//根据点击数排序文章列表

    @Select("select noticeId,title,authorId,authorName,`desc`,createTime from notice order by createTime desc limit #{offset}, #{pagesPerPage}")
    List<Article> getListByTime_notice(@Param("offset")Integer offset, @Param("pagesPerPage")Integer pagesPerPage);//根据时间排序文章列表

    @Select("select count(articleId) FROM article")
    int getArticleNum();//计算文章数量

    @Select("select count(noticeId) FROM notice")
    int getNoticeNum();//计算文章数量

}
