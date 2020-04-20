package com.example.techassessment.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("articles")
public class Article {
    @Id
    private String id;
    private Integer articleId;
    @NotBlank
    private String title = "";
    @NotBlank
    private String summary = "";
    private List<Section> sections;

    public void addSection(final Section section) {
        if (this.sections == null) {
            this.sections = new ArrayList<>();
        }
        this.sections.add(section);
    }
}