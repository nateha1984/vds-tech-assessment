package com.example.techassessment.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.techassessment.model.Article;
import com.example.techassessment.model.Section;
import com.example.techassessment.service.ArticleService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/articles")
@RestController
@RequiredArgsConstructor
public class WikiController {

    private final ArticleService articleService;

    @GetMapping
    public List<Article> getArticle(@RequestParam("articleContent") final String content) {
        if (StringUtils.isNotBlank(content)) {
            return articleService.findArticlesContaining(content);
        } else {
            return articleService.getArticles();
        }
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable final Integer id) {
        return articleService.findArticleById(id);
    }

    @GetMapping("/{articleId}/sections")
    public List<Section> getSections(@PathVariable final Integer articleId, @RequestParam(name = "searchTerm", required = false) final String searchTerm) {
        if (StringUtils.isNotBlank(searchTerm)) {
            return articleService.findSectionsContaining(articleId, searchTerm);
        } else {
            return articleService.getArticleSections(articleId);
        }
    }

    @GetMapping("/{articleId}/sections/{sectionId}")
    public Section getSectionById(@PathVariable("articleId") final Integer articleId, @PathVariable("sectionId") final String sectionId) {
        return articleService.getArticleSectionBySectionId(articleId, sectionId);
    }

    @PostMapping
    public Article createNewArticle(final @Valid Article article) {
        final Article dummyArticle = new Article();
        dummyArticle.setArticleId(1000);
        dummyArticle.setTitle("Too Bad: We're Not Able To Add New Articles At This Time");
        dummyArticle.setSummary("Due to budget constraints, the ability to add new articles is on hold. Please check back at a later time. Should you find this message inconvenient, please contact <a href=\"http://voith.com/corp-en/industry-solutions___internet-of-things.html\">Voith Digital</a> and tell them to hire Nate Hall so he can finish this up.");
        return dummyArticle;
    }
}