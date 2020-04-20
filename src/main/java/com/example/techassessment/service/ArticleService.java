package com.example.techassessment.service;

import java.util.List;

import com.example.techassessment.model.Article;
import com.example.techassessment.model.Section;

public interface ArticleService {
    void addArticle(final Article Article);
    List<Article> getArticles();
	Article findArticleById(Integer id);
	List<Section> getArticleSections(Integer articleId);
	Section getArticleSectionBySectionId(Integer articleId, String sectionId);
	List<Section> findSectionsContaining(Integer articleId, String sectionContent);
	List<Article> findArticlesContaining(String content);
}