package com.example.techassessment;

import com.example.techassessment.model.Article;
import com.example.techassessment.model.Section;
import com.example.techassessment.service.ArticleService;

import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {

    private static final String H4_TAG = "h4";
    private static final String SECTION_ID_DELIMITER = "-";
    private static final String NEWLINE = "\n";
    private static final String OL_TAG = "ol";
    private static final String DIV_TAG = "div";
    private static final String H3_TAG = "h3";
    private static final String UL_TAG = "ul";
    private static final String HEADER_NAME_CLASS = ".mw-headline";
    private static final String P_TAG = "p";
    private static final String H2_TAG = "h2";

    private final ArticleService articlesService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Heidenheim_an_der_Brenz").get();
        Element mainBody = doc.getElementById("mw-content-text").child(0);
        final Elements elements = mainBody.children();
        final Article article = new Article();
        article.setTitle(doc.getElementById("firstHeading").text());
        article.setArticleId(1);
        int h2Counter = 1;
        Element element = elements.first();
        while (element != null) {
            if (CollectionUtils.isEmpty(article.getSections()) && element.nodeName().equals(P_TAG)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(article.getSummary()).append(element.text()).append(NEWLINE);
                article.setSummary(sb.toString());
                element = element.nextElementSibling();
            } else if (element.nodeName().equals(H2_TAG)) {
                final Section h2 = new Section();
                h2.setSectionId(String.valueOf(h2Counter++));
                h2.setSectionName(element.children().select(HEADER_NAME_CLASS).text());
                element = element.nextElementSibling();
                int h3SectionId = 1;
                while (element != null && !element.nodeName().equals(H2_TAG)) {
                    switch (element.nodeName()) {
                        case P_TAG:
                            h2.addToContent(element.text());
                            element = element.nextElementSibling();
                            break;
                        case UL_TAG:
                            element = handleList(h2, element);
                            break;
                        case H3_TAG:
                            element = addH3Section(article, element, h3SectionId++, h2.getSectionId());
                            break;
                        case DIV_TAG:
                            element = handleWrappedLists(h2, element);
                            break;
                        default:
                            element = element.nextElementSibling();
                            break;
                    }
                }
                article.addSection(h2);
            } else {
                element = element.nextElementSibling();
            }
        }
        articlesService.addArticle(article);
    }

    private Element handleWrappedLists(Section h2, Element divElement) {
        if (divElement.child(0) != null && divElement.child(0).nodeName().equals(UL_TAG)) {
            return handleList(h2, divElement.child(0));
        } else if (divElement.hasClass("refList")) {
            Elements list = divElement.select(OL_TAG);
            return handleList(h2, list.first());
        } else {
            return divElement.nextElementSibling();
        }
    }

    private Element handleList(Section section, Element listElement) {
        final StringBuilder sb = new StringBuilder();
        for (Element e : listElement.children()) {
            sb.append(section.getContent()).append(e.text()).append(NEWLINE);
        }
        section.addToContent(sb.toString());
        return listElement.nextElementSibling();
    }

    private Element addH3Section(Article article, Element element, int h3SectionId, String h2SectionId) {
        final Section h3 = new Section();
        h3.setSectionId(h2SectionId.concat(SECTION_ID_DELIMITER).concat(String.valueOf(h3SectionId)));
        h3.setSectionName(element.children().select(HEADER_NAME_CLASS).text());
        element = element.nextElementSibling();
        int h4SectionId = 1;
        while (element != null && !element.nodeName().equals(H3_TAG)
                && !element.nodeName().equals(H2_TAG)) {
            switch (element.nodeName()) {
                case P_TAG:
                    h3.addToContent(element.text());
                    element = element.nextElementSibling();
                    break;
                case UL_TAG:
                    element = handleList(h3, element);
                    break;
                case H4_TAG:
                    element = addH4Section(article, element, h4SectionId++, h3.getSectionId());
                    break;
                default:
                    element = element.nextElementSibling();
                    break;
            }
        }
        article.addSection(h3);
        return element;
    }

    private Element addH4Section(Article article, Element element, int h4SectionId, String h3Id) {
        final Section h4 = new Section();
        h4.setSectionId(h3Id.concat(SECTION_ID_DELIMITER).concat(String.valueOf(h4SectionId)));
        h4.setSectionName(element.children().select(HEADER_NAME_CLASS).text());
        element = element.nextElementSibling();
        while (element != null 
                && !element.nodeName().equals(H4_TAG)
                && !element.nodeName().equals(H3_TAG)
                && !element.nodeName().equals(H2_TAG)) {
            if (element.nodeName().equals(P_TAG)) {
                h4.addToContent(element.text());
            }
            element = element.nextElementSibling();
        }
        article.addSection(h4);
        return element;
    }
}