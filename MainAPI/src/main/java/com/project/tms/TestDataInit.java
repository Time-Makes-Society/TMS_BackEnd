//package com.project.tms;
//
//import com.project.tms.domain.Article;
//import com.project.tms.domain.UUIDArticle;
//import com.project.tms.dto.MemberDto;
//import com.project.tms.repository.ArticleRepository;
//import com.project.tms.repository.UUIDArticleRepository;
//import com.project.tms.service.MemberService;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class TestDataInit {
//
//    private final MemberService memberService;
////    private final UUIDArticleRepository uuidArticleRepository;
//
//    @PostConstruct
//    public void init() {
//        createTestMember();
////        createTestArticle();
//    }
//
//    private void createTestMember() {
//
//        MemberDto memberDto = new MemberDto();
//        memberDto.setLoginId("test");
//        memberDto.setPassword("test!");
//        memberDto.setMemberName("testUser");
//
//        memberService.join(memberDto);
//    }
//
////    private void createTestArticle() {
////        UUIDArticle uuidArticle = new UUIDArticle();
////        uuidArticle.setId(UUID.randomUUID());
////        uuidArticle.setTitle("test Article");
////        uuidArticle.setCreatedDate("test date");
////        uuidArticle.setArticleTime("test read time");
////        uuidArticle.setContent("test content");
////        uuidArticle.setLink("test link");
////        uuidArticle.setCategory("test category");
////        uuidArticle.setImage("test image");
////
////        uuidArticleRepository.save(uuidArticle);
////    }
//}
