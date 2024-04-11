package com.project.tms;

import com.project.tms.domain.Article;
import com.project.tms.dto.MemberDto;
import com.project.tms.repository.ArticleRepository;
import com.project.tms.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;
    private final ArticleRepository articleRepository;

    @PostConstruct
    public void init() {
        createTestMember();
        createTestArticle();
    }

    private void createTestMember() {

        MemberDto memberDto = new MemberDto();
        memberDto.setLoginId("test");
        memberDto.setPassword("test!");
        memberDto.setMemberName("testUser");

        memberService.join(memberDto);
    }

    private void createTestArticle() {
        Article article = new Article();
        article.setId(UUID.randomUUID());
        article.setTitle("test Article");
        article.setCreatedDate("test date");
        article.setArticleTime("test read time");
        article.setContent("test content");
        article.setLink("test link");
        article.setCategory("test category");
        article.setImage("test image");

        articleRepository.save(article);
    }
}
