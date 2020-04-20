package com.example.techassessment.repository;

import java.util.Optional;

import com.example.techassessment.model.Article;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticlesRepository extends MongoRepository<Article, String> {

	Optional<Article> findOneByArticleId(Integer id);

}