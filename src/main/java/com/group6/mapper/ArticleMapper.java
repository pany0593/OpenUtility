package com.group6.mapper;

import com.group6.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ArticleMapper {//Article表
    @Insert("INSERT INTO Article (articleId, authorId, title, `desc`, content,clicks,createTime) " +
            "VALUES (#{articleId}, #{authorId}, #{title}, #{desc}, #{content},#{clicks},CURRENT_TIMESTAMP")
    int insertArticle(String articleId, String authorId, String title, String content, String desc);//增加文章

    @Delete("DELETE FROM Article WHERE articleId = #{articleId}")
    int deleteArticle(@Param("articleId") String articleId);//删除文章

    @Update("UPDATE Article SET title = #{title}, `desc` = #{desc}, content = #{content} WHERE articleId = #{articleId}")
    int updateArticle(Article article);//修改文章

    @Select("SELECT articleId,title,authorName,`desc`,createTime,likes,clicks from Article where articleId = #{articleId}")
    Article findByArticleId(String articleId);//根据文章id返回文章搜索结果

    @Update("UPDATE Article set likes = likes + 1 where articleId = #{articleId}")
    void likeArticle(String articleId);//点赞文章

    @Update("UPDATE Article set clicks = clicks + 1 where articleId = #{articleId}")
    void addClicks(String articleId);//增加文章点击

    @Select("select articleId,title,authorId,`desc`,createTime,likes,clicks from Article order by createTime desc limit #{offset}, #{pagesPerPage}")
    List<Article> getListByTime(Integer offset,Integer pagesPerPage);//根据时间排序文章列表

    @Select("select articleId,title,authorId,`desc`,createTime,likes,clicks from Article order by clicks desc limit #{offset}, #{pagesPerPage}")
    List<Article> getListByClick(Integer offset,Integer pagesPerPage);//根据点击数排序文章列表

    @Select("select count(articleId) FROM article")
    int getArticleNum();//计算文章数量

}
