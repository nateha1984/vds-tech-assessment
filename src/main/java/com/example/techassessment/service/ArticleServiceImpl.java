package com.example.techassessment.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.techassessment.exception.NotFoundException;
import com.example.techassessment.model.Article;
import com.example.techassessment.model.Section;
import com.example.techassessment.repository.ArticlesRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticlesRepository articlesRepository;

    @Override
    public void addArticle(final Article article) {
        articlesRepository.save(article);
    }

    @Override
    public List<Article> getArticles() {
        return articlesRepository.findAll();
    }

    @Override
    public Article findArticleById(final Integer id) {
        return articlesRepository.findOneByArticleId(id).orElseThrow(() -> new NotFoundException());
    }

    @Override
    public List<Section> getArticleSections(final Integer articleId) {
        final Article article = findArticleById(articleId);
        return article.getSections();
    }

    @Override
    public Section getArticleSectionBySectionId(final Integer articleId, final String sectionId) {
        final List<Section> section = findArticleById(articleId).getSections().stream()
                .filter(sec -> sec.getSectionId().equals(sectionId)).collect(Collectors.toList());
        if (section.isEmpty()) {
            throw new NotFoundException();
        } else {
            return section.get(0);
        }
    }

    @Override
    public List<Section> findSectionsContaining(final Integer articleId, final String searchTerm) {
        final Article article = findArticleById(articleId);
        return article.getSections().parallelStream()
        .filter(section -> {
            return sectionContains(section, searchTerm);
        })
        .collect(Collectors.toList());
    }

    @Override
    public List<Article> findArticlesContaining(String content) {
        return articlesRepository.findAll().stream()
        .filter(article -> {
            return article.getSummary().toLowerCase().contains(content) || article.getSections().stream().anyMatch(section -> sectionContains(section, content));
        })
        .collect(Collectors.toList());
        
    }

    private boolean sectionContains(final Section section, final String searchTerm) {
        return (section.getSectionName().toLowerCase().contains(searchTerm) || section.getContent().toLowerCase().contains(searchTerm)); 
    }
}