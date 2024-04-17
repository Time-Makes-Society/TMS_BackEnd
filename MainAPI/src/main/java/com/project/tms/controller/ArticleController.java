package com.project.tms.controller;


import com.project.tms.domain.Article;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.UUIDArticleDTO;
import com.project.tms.service.ArticleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    // 플라스크 전처리 서버를 작동시켜 pre_news형태의 엔티티를 UUID가 붙은 news 엔티티로 변환해 테이블에 저장하는 메서드
    @GetMapping("/firstAPI/{category}")
    public ResponseEntity<String> refreshAndConvertToUUID(@PathVariable(name = "category") String category) throws UnsupportedEncodingException {

        // http://localhost:8081/newssave/category로 엔트포인트로 GET 요청을 보냄
        String encodedCategory = URLEncoder.encode(category, StandardCharsets.UTF_8);
        String flaksServerUrl = "http://localhost:8081/newssave/" + encodedCategory;
        articleService.sendGetRequestToFlask(flaksServerUrl);

        // 모든 pre_news에 있는 데이터를 가져옴
        List<Article> articles = articleService.preNewsDBFindAll();

        // 데이터를 읽어와서 새로운 엔티티를 생성하고 저장
        articleService.oldEntityToNewEntity(articles);

        return ResponseEntity.ok("스프링 서버에 저장이 완료되었습니다.");
    }


    // UUID이 붙은 Article를 카테고리를 설정해서 리스트로 가져오는 메서드
    @GetMapping("/uuid")
    public ResponseEntity<List<Page<UUIDArticleDTO>>> getUUIDArticlesByCategories(@RequestParam(value = "category", required = false) String categoryString,
                                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                  @RequestParam(value = "target", required = false) String targetTime,
                                                                                  Pageable pageable) {
        // 쿼리스트링이 비어있는 경우 기본값 설정
        if (categoryString == null || categoryString.isEmpty()) {
            categoryString = ""; // 빈 문자열로 설정하여 아무 카테고리도 선택되지 않은 것으로 간주
        }

        // 쉼표로 구분된 카테고리 목록을 파싱
        String[] categories = categoryString.split(",");

        // 페이징 결과를 저장할 리스트
        List<Page<UUIDArticleDTO>> result = new ArrayList<>();

        // 동적 페이징 설정
        int pageSize = pageable.getPageSize(); // 동적인 pagable의 객체의 쿼리 스트링
        int pageNumber = page; // page 쿼리 스트링
        int maxPageSize = 5; // 최대 아이템 개수
        int adjustedPageSize = Math.min(pageSize, maxPageSize); // 둘 중 더 작은 page의 수를 적용

        // 페이징 번호, 한 페이지의 갯수, pageable 객체의 정렬을 이용해 pageable 객체 생성
        Pageable pageableWithAdjustedSize = PageRequest.of(pageNumber, adjustedPageSize, pageable.getSort());

        // category 쿼리스트링이 비어있는 경우 모든 category의 데이터를 조회
        if (categoryString.isEmpty()) {
            // 가공한 모든 데이터를 받아옴
            Page<UUIDArticle> uuidArticles = articleService.noCategoryFindAll(pageableWithAdjustedSize);

            // 각 페이지에 맞게 DTO로 변환
            Page<UUIDArticleDTO> dtoPage = articleService.entityToPageDTO(uuidArticles);

            // 페이징 결과 리스트에 데이터를 추가
            result.add(dtoPage);

        } else {  // 각 category별로 데이터 가져오기

            // 리스트 스플릿으로 자른 것을 category 변수로 순회
            for (String category : categories) {
                // 최신순으로 category 변수를 순회
                Page<UUIDArticle> uuidArticles = articleService.manyCategoryfindAll(category, pageableWithAdjustedSize);

                // 각 페이지에 맞게 DTO로 변환
                Page<UUIDArticleDTO> dtoPage = articleService.entityToPageDTO(uuidArticles);

                // mm:ss 형식의 target 쿼리스트링이 주어진 경우
                log.info("target: {}", targetTime);

                if (targetTime != null && !targetTime.isEmpty()) {
                    // 시간대가 가장 근접한 기사들을 가져오는 메서드 호출
                    List<UUIDArticleDTO> closestArticles = articleService.findClosestToTargetTimeByCategory(category, targetTime, adjustedPageSize, pageNumber);

                    // 가장 근접한 기사들의 페이지 생성
                    Page<UUIDArticleDTO> closestPage = new PageImpl<>(closestArticles, pageableWithAdjustedSize, closestArticles.size());
                    // 페이징 결과 리스트에 데이터를 추가
                    result.add(closestPage);
                } else {
                    // 타겟 시간이 없는 경우 기존의 결과를 그대로 사용
                    result.add(dtoPage);
                }
            }
        }

        return ResponseEntity.ok(result);
    }


}
