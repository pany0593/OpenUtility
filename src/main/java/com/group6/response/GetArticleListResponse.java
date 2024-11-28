package com.group6.response;

import com.group6.pojo.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetArticleListResponse {
    private List<Article> articles;
    private Integer totalPages;
}
