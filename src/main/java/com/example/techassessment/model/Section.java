package com.example.techassessment.model;

import lombok.Data;

@Data
public class Section {
    private String sectionId;
    private String sectionName = "";
    private String content = "";

    public void addToContent(final String newParagraph) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.content).append(newParagraph).append("\n");
        this.content = sb.toString();
    }
}
