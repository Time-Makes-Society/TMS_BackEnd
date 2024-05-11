package com.project.tms.controller;


import com.project.tms.domain.Article;
import com.project.tms.domain.Member;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.*;
import com.project.tms.service.ArticleLikeService;
import com.project.tms.service.ArticleService;
import com.project.tms.web.login.SessionConst;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleLikeService articleLikeService;


    // 플라스크 전처리 서버를 작동시켜 pre_news형태의 엔티티를 UUID가 붙은 news 엔티티로 변환해 테이블에 저장하는 메서드
    @GetMapping("/firstAPI/{category}")
    public ResponseEntity<String> sendFlaskAndConvertToUUID(@PathVariable(name = "category") String category) throws UnsupportedEncodingException {

        // Flask 서버 엔트포인트로 GET 요청을 보냄
        String encodedCategory = URLEncoder.encode(category, StandardCharsets.UTF_8);
        String flaksServerUrl = "http://localhost:8081/newssave/" + encodedCategory;
//        String flaksServerUrl = "https://quh62kky3f.execute-api.ap-northeast-2.amazonaws.com/dev/newssave/" + encodedCategory;
        articleService.sendGetRequestToFlask(flaksServerUrl);

        // 모든 pre_news에 있는 데이터를 가져옴
        List<Article> articles = articleService.preNewsDBFindAll();

        // 데이터를 읽어와서 새로운 엔티티를 생성하고 저장
        articleService.oldEntityToNewEntity(articles);

        return ResponseEntity.ok("스프링 서버에 저장이 완료되었습니다.");
    }


    @GetMapping("/articles")
    public ResponseEntity<Map<String, Object>> getUUIDArticlesByCategories(@RequestParam(value = "category", required = false) String categoryString, @RequestParam(value = "page", defaultValue = "0") int page, Pageable pageable) {
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

        // 페이지 번호, 한 페이지의 아이템 개수 설정
        int pageCurrentSize = 0; // 현재 페이지의 아이템 개수

        // category 쿼리스트링이 비어있는 경우 모든 category의 데이터를 조회
        if (categoryString.isEmpty()) {
            // 가공한 모든 데이터를 받아옴
            Page<UUIDArticle> uuidArticles = articleService.noCategoryFindAll(PageRequest.of(pageNumber, adjustedPageSize, pageable.getSort()));
            // 각 페이지에 맞게 DTO로 변환
            Page<UUIDArticleListDto> dtoPage = articleService.entityToPageDto(uuidArticles);
            // 페이징 결과 리스트에 데이터를 추가
            result.add(dtoPage);
            pageCurrentSize = dtoPage.getNumberOfElements();
        } else {  // 각 category별로 데이터 가져오기
            // 리스트 스플릿으로 자른 것을 category 변수로 순회
            for (String category : categories) {
                // 최신순으로 category 변수를 순회
                Page<UUIDArticle> uuidArticles = articleService.manyCategoryFindAll(category, PageRequest.of(pageNumber, adjustedPageSize, pageable.getSort()));
                // 각 페이지에 맞게 DTO로 변환
                Page<UUIDArticleListDto> dtoPage = articleService.entityToPageDto(uuidArticles);
                result.add(dtoPage);
                pageCurrentSize += dtoPage.getNumberOfElements();
            }
        }

        long totalElements = 0;
        if (!categoryString.isEmpty()) {
            totalElements = articleService.countByCategoryIn(Arrays.asList(categories));
        } else {
            totalElements = articleService.countAllArticles(); // 카테고리가 없는 경우 전체 기사 수를 구함
        }

        // totalPages 계산
        int totalPages = (int) Math.ceil((double) totalElements / (adjustedPageSize * categories.length));

        // 페이지 정보 설정
        Map<String, Object> response = new HashMap<>();
        List<UUIDArticleListDto> articles = result.stream().flatMap(pageData -> pageData.getContent().stream()).collect(Collectors.toList());
        ArticlePageDto articlePageDto = new ArticlePageDto(pageNumber, adjustedPageSize * categories.length, pageCurrentSize, totalPages, totalElements);
        response.put("articles", articles);
        response.put("pageInfo", articlePageDto);

        return ResponseEntity.ok(response);
    }

    // 기사 하나 조회
    @GetMapping("/articles/{uuid}")
    public ResponseEntity<Object> getUUIDArticlesDetail(@PathVariable(name = "uuid") UUID uuid) {
        try {
            UUIDArticle uuidArticle = articleService.articleFindOne(uuid).orElseThrow(() -> new EntityNotFoundException());
            UUIDArticleDetailDto uuidArticleDetailDto = articleService.entityToDetailDto(uuidArticle);

            return ResponseEntity.ok().body(uuidArticleDetailDto);
        } catch (EntityNotFoundException e) {
            // EntityNotFoundException 발생 시 404 에러를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id에 기사를 찾지 못했습니다");
        }
    }

    // 추천 기사 조회
    @GetMapping("/articles/recommend")
    public ResponseEntity<Object> getUUIDArticlesByCategoriesResultTarget(@RequestParam(value = "category", required = true) String category, @RequestParam(value = "target", required = true) String target, Pageable pageable) {
        try {
            // mm:ss 형식의 target 쿼리스트링이 주어진 경우
            log.info("target: {}", target);

            // 쉼표로 구분된 카테고리 목록을 파싱
            String[] categories = category.split(",");

            log.info("categories: {}", Arrays.toString(categories));

            // ":"를 구분자로 사용하여 분과 초를 분리
            String[] timeParts = target.split(":");

            // 잘못된 형식의 target 파라미터일 때 에러 처리
            if (timeParts.length != 2) {
                return ResponseEntity.badRequest().body(Collections.singletonList("잘못된 시간 형식입니다."));
            }

            int minutes = Integer.parseInt(timeParts[0]); // 분
            int seconds = Integer.parseInt(timeParts[1]); // 초

            // 시간 대신 분과 초만을 사용하여 LocalTime 객체 생성
            LocalTime targetTime = LocalTime.of(0, minutes, seconds);

            // 최대 페이지 크기를 설정
            int adjustedPageSize = pageable.getPageSize();

            // 지정된 카테고리 및 대상 시간을 기반으로 가장 가까운 기사를 찾는 메서드를 호출
            List<UUIDArticleListDto> closestArticles = articleService.findClosestToTargetTimeByCategories(categories, targetTime, adjustedPageSize);

            // 추천기사 리스트가 비어 있는 경우
            if (closestArticles.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singleton("추천할 수 있는 기사가 없습니다."));
            }

            return ResponseEntity.ok(closestArticles);
        } catch (IllegalArgumentException e) { // 잘못된 형식의 카테고리일 때 에러 처리
            return ResponseEntity.badRequest().body(Collections.singleton("잘못된 파라미터 형식입니다."));
        }
    }

    @PostMapping("/articles/like/{uuid}")
    public ResponseEntity<String> likeArticle(@PathVariable UUID uuid, HttpSession session) {
        try {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {
                Long memberId = loginMember.getId();
                articleLikeService.likeArticle(uuid, memberId);
                return ResponseEntity.ok("좋아요 완료");
            } else {
                // 로그인되지 않은 사용자에 대한 처리
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping("/articles/like/{uuid}")
    public ResponseEntity<Object> getLikeCount(@PathVariable UUID uuid, HttpSession session) {
        try {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {
                Long memberId = loginMember.getId();
                LikeCountDto likeCountDto = articleLikeService.getLikeCount(uuid, memberId);
                return ResponseEntity.ok(likeCountDto);
            } else {
                // 로그인되지 않은 사용자에 대한 처리
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @DeleteMapping("/articles/like/{uuid}")
    public ResponseEntity<String> cancelLikeArticle(@PathVariable UUID uuid, HttpSession session) {
        try {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

            if (loginMember != null) {
                Long memberId = loginMember.getId();
                articleLikeService.cancelLikeArticle(uuid, memberId);
                return ResponseEntity.ok("좋아요 취소 완료");
            } else {
                // 로그인되지 않은 사용자에 대한 처리
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }


    @GetMapping("articles/like")
    public ResponseEntity<Object> getLikedArticles(HttpSession session) {
        try {
            Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

            // 좋아요한 기사 엔티티를 리스트로 가져옴
            List<UUIDArticle> likedArticles = articleLikeService.getLikedArticlesByMemberId(member.getId());

            // UUIDArticle 엔티티 정보를 dto로 변환
            List<UUIDArticleListDto> articleListDtos = articleLikeService.entityToDto(likedArticles);

            // UUIDArticle 엔티티 정보를 dto로 변환
            LikedArticleDto likedArticleDto = new LikedArticleDto();
            likedArticleDto.setLikedArticleList(articleListDtos);

            return ResponseEntity.ok(likedArticleDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}