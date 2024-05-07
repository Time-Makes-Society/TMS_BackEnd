package com.project.tms.controller;


import com.project.tms.domain.Article;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.UUIDArticleDetailDto;
import com.project.tms.dto.UUIDArticleListDto;
import com.project.tms.service.ArticleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    // 플라스크 전처리 서버를 작동시켜 pre_news형태의 엔티티를 UUID가 붙은 news 엔티티로 변환해 테이블에 저장하는 메서드
    @GetMapping("/firstAPI/{category}")
    public ResponseEntity<String> sendFlaskAndConvertToUUID(@PathVariable(name = "category") String category) throws UnsupportedEncodingException {

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


    @GetMapping("/articles")
    public ResponseEntity<List<Page<UUIDArticleListDto>>> getUUIDArticlesByCategories(@RequestParam(value = "category", required = false) String categoryString,
                                                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                      Pageable pageable) {
        // 쿼리스트링이 비어있는 경우 기본값 설정
        if (categoryString == null || categoryString.isEmpty()) {
            categoryString = ""; // 빈 문자열로 설정하여 아무 카테고리도 선택되지 않은 것으로 간주
        }

        // 쉼표로 구분된 카테고리 목록을 파싱
        String[] categories = categoryString.split(",");

        // 페이징 결과를 저장할 리스트
        List<Page<UUIDArticleListDto>> result = new ArrayList<>();

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
            Page<UUIDArticleListDto> dtoPage = articleService.entityToPageDTO(uuidArticles);


            // 페이징 결과 리스트에 데이터를 추가
            result.add(dtoPage);

        } else {  // 각 category별로 데이터 가져오기
            // 리스트 스플릿으로 자른 것을 category 변수로 순회
            for (String category : categories) {
                // 최신순으로 category 변수를 순회
                Page<UUIDArticle> uuidArticles = articleService.manyCategoryFindAll(category, pageableWithAdjustedSize);

                // 각 페이지에 맞게 DTO로 변환
                Page<UUIDArticleListDto> dtoPage = articleService.entityToPageDTO(uuidArticles);

                result.add(dtoPage);
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/articles/{uuid}")
    public ResponseEntity<UUIDArticleDetailDto> getUUIDArticlesDetail(@PathVariable(name = "uuid") UUID uuid) {
        UUIDArticleDetailDto uuidArticleDetailDTO = articleService.articleFindOne(uuid);

        return ResponseEntity.ok().body(uuidArticleDetailDTO);
    }

    @GetMapping("/articles/recommend")
    public ResponseEntity<List<UUIDArticleListDto>> getUUIDArticlesByCategoriesResultTarget(
            @RequestParam(value = "category", required = true) String category,
            @RequestParam(value = "target", required = true) String target,
            Pageable pageable) {

        // mm:ss 형식의 target 쿼리스트링이 주어진 경우
        log.info("target: {}", target);

        // 쉼표로 구분된 카테고리 목록을 파싱
        String[] categories = category.split(",");

        log.info("categories: {}", Arrays.toString(categories));

        // ":"를 구분자로 사용하여 분과 초를 분리
        String[] timeParts = target.split(":");
        int minutes = Integer.parseInt(timeParts[0]); // 분
        int seconds = Integer.parseInt(timeParts[1]); // 초

        // 시간 대신 분과 초만을 사용하여 LocalTime 객체 생성
        LocalTime targetTime = LocalTime.of(0, minutes, seconds);


        // 최대 페이지 크기를 설정
        int adjustedPageSize = pageable.getPageSize();

        // 지정된 카테고리 및 대상 시간을 기반으로 가장 가까운 기사를 찾는 메서드를 호출
        List<UUIDArticleListDto> closestArticles = articleService.findClosestToTargetTimeByCategories(categories, targetTime, adjustedPageSize);

        return ResponseEntity.ok(closestArticles);
    }
}