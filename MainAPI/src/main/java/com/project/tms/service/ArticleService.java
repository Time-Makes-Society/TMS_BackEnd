package com.project.tms.service;


import com.project.tms.domain.Article;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.flask.FlaskResponse;
import com.project.tms.dto.UUIDArticleDetailDto;
import com.project.tms.dto.UUIDArticleListDto;
import com.project.tms.dto.flask.RecommendArticleDto;
import com.project.tms.repository.ArticleRepository;
import com.project.tms.repository.UUIDArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArticleService {


    private final UUIDArticleRepository uuidArticleRepository;

    private final ArticleRepository articleRepository;

    private final GptService gptService;

    private final RestTemplate restTemplate;


    // 모든 pre_news에 있는 데이터를 가져오는 메서드
    public List<Article> preNewsDBFindAll() {
        return articleRepository.findAll();
    }


    // 모든 가공한 news 테이블 데이터를 최신순으로 가져오는 메서드
    public Page<UUIDArticle> noCategoryFindAll(Pageable pageable) {
        return uuidArticleRepository.findAllOrderByCreatedDateDesc(pageable);
    }

    public Page<UUIDArticle> manyCategoryFindAll(String category, Pageable pageable) {
        return uuidArticleRepository.findByCategoryOrderByCreatedDateDesc(category, pageable);
    }


    public Optional<UUIDArticle> articleFindOne(UUID uuid) {
        return uuidArticleRepository.findById(uuid);
    }

    // 임베딩 값을 필드에 저장시키는 메서드
    public void saveEmbedding(UUIDArticle uuidArticle, String embedding) {
        uuidArticle.setEmbedding(embedding);
        uuidArticleRepository.save(uuidArticle);
    }

    // 하나 기사 데이터들을 엔티티에서 dto로 변환하는 메서드
    public UUIDArticleDetailDto entityToDetailDto(UUIDArticle uuidArticle) {
        UUIDArticleDetailDto articleDetailDTO = new UUIDArticleDetailDto();

        articleDetailDTO.setId(uuidArticle.getId());
        articleDetailDTO.setTitle(uuidArticle.getTitle());
        articleDetailDTO.setContent(uuidArticle.getContent());
        articleDetailDTO.setGptContent(uuidArticle.getGptContent());
        articleDetailDTO.setSimilarity(uuidArticle.getDiffContentEmbedding());
        articleDetailDTO.setCategory(uuidArticle.getCategory());
        articleDetailDTO.setImage(uuidArticle.getImage());
        articleDetailDTO.setLink(uuidArticle.getLink());
        articleDetailDTO.setArticleTime(uuidArticle.getArticleTime());
        articleDetailDTO.setLikeCount(uuidArticle.getLikeCount());

        LocalDateTime createdDate = uuidArticle.getCreatedDate();
        articleDetailDTO.setCreatedDate(createdDate);

        return articleDetailDTO;
    }


    // 가공한 news 테이블 데이터를 PageDTO 형식으로 재가공하는 메서드
    public Page<UUIDArticleListDto> entityToPageDto(Page<UUIDArticle> articlePage) {
        return articlePage.map(uuidArticle -> {
            UUIDArticleListDto dto = new UUIDArticleListDto();
            dto.setId(uuidArticle.getId());
            dto.setTitle(uuidArticle.getTitle());
            dto.setCategory(uuidArticle.getCategory());
            dto.setImage(uuidArticle.getImage());
            dto.setArticleTime(uuidArticle.getArticleTime());
            dto.setPublisher(uuidArticle.getPublisher());

            LocalDateTime createdDate = uuidArticle.getCreatedDate();
            dto.setCreatedDate(createdDate);
            return dto;
        });
    }


    // 엔티티를 UUIDArticleListDto 형식으로 변환하는 메서드
    private UUIDArticleListDto entityToDto(UUIDArticle uuidArticle) {
        UUIDArticleListDto dto = new UUIDArticleListDto();
        dto.setId(uuidArticle.getId());
        dto.setTitle(uuidArticle.getTitle());
        dto.setCategory(uuidArticle.getCategory());
        dto.setImage(uuidArticle.getImage());
        dto.setArticleTime(uuidArticle.getArticleTime());
        dto.setPublisher(uuidArticle.getPublisher());

        LocalDateTime createdDate = uuidArticle.getCreatedDate();
        dto.setCreatedDate(createdDate);
        return dto;
    }

    // 엔티티를 UUIDArticleListDto 리스트 형식로 변환하는 메서드
    private List<UUIDArticleListDto> entityToListDto(List<UUIDArticle> articles, int pageSize) {
        int startIndex = 0;
        int endIndex = Math.min(pageSize, articles.size());

        return articles.subList(startIndex, endIndex)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // 카테고리별 기사 수를 조회하는 메서드
    public long countByCategoryIn(List<String> categories) {
        return uuidArticleRepository.countByCategoryIn(categories);
    }

    // 전체 기사 수를 조회하는 메서드
    public long countAllArticles() {
        return uuidArticleRepository.count();
    }

    // 데이터를 읽어와서 새로운 엔티티를 생성하고 저장하는 메서드
    public void oldEntityToNewEntity(List<Article> articles) {
        for (Article article : articles) {

            // 기존 Article 엔티티에서 필요한 데이터를 가져와서 UUIDArticle을 생성
            List<UUIDArticle> existingArticle = uuidArticleRepository.findByTitle(article.getTitle());

            // 이미 저장된 기사인지 확인
            if (!existingArticle.isEmpty()) {
                // 이미 저장된 기사인 경우 다음 기사로 넘어감
                continue;
            }

            // 기존 Article 엔티티에서 필요한 데이터를 가져와서 UUIDArticle을 생성
            UUIDArticle uuidArticle = new UUIDArticle();
            uuidArticle.setTitle(article.getTitle());
            uuidArticle.setContent(article.getContent());
            uuidArticle.setCreatedDate(article.getCreatedDate());
            uuidArticle.setPublisher(article.getPublisher());
            uuidArticle.setCategory(article.getCategory());
            uuidArticle.setImage(article.getImage());
            uuidArticle.setLink(article.getLink());

            // content의 글자 수 계산
            int contentLength = article.getContent().length();

            // 한국 사람이 평균적으로 1분에 읽는 글자 수(1분에 1000자)
            int charsPerMinute = 1000;

            // content의 글자 수를 평균 읽기 속도로 나누어서 읽는 시간 계산
            int readingTimeInMinutes = (int) Math.ceil((double) contentLength / charsPerMinute);

            // 계산된 읽는 시간을 분과 초로 변환하여 저장
            int minutes = readingTimeInMinutes % 60;
            int seconds = (int) ((contentLength % charsPerMinute) * 60.0 / charsPerMinute);

            // 시간 정보를 LocalTime 객체로 변환
            LocalTime articleTime = LocalTime.of(0, minutes, seconds);
            uuidArticle.setArticleTime(articleTime);

            String summarizeContent = gptService.summarizeContent(article.getContent()); // gpt한테 요약을 요청함
            uuidArticle.setGptContent(summarizeContent);


            // 먼저 uuidArticle을 저장하여 ID를 생성
            uuidArticleRepository.save(uuidArticle);

            // title, content, gptContent 임베딩 값을 계산하고 저장
            gptService.calculateAndSaveTitleEmbedding(uuidArticle);

            gptService.calculateAndSaveContentEmbedding(uuidArticle);

            gptService.calculateAndSaveGptContentEmbedding(uuidArticle);

            calculateEmbedding(uuidArticle);
        }
    }


    private void calculateEmbedding(UUIDArticle article) {
        Long similarity = gptService.calculateEmbeddingDiffContent(article.getId()); // uuid를 사용하여 유사도 계산

        article.setDiffContentEmbedding(similarity);

        // 다시 저장하여 업데이트된 내용 반영
        uuidArticleRepository.save(article);
    }

    // 플라스크 전처리 서버에 HTTP GET 요청 보내는 메서드
    public void sendGetRequestToFlask(String url) {
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 응답 처리
                log.info("플라스크 서버 응답: {}", response.toString());
            } else {
                log.info("GET 요청 실패:", responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<RecommendArticleDto> fetchAndMapArticles(String url) {
        FlaskResponse flaskResponse = fetchFlaskResponse(url);
        if (flaskResponse == null) {
            return null;
        }

        return flaskResponse.getRecommendedArticles().stream()
                .map(recommendedArticle -> {
                    UUIDArticle article = uuidArticleRepository.findById(recommendedArticle.getUuid()).orElse(null);
                    if (article == null) {
                        return null;
                    }

                    // similarity 값을 변환하여 문자열로 포맷팅
                    String similarityPercentage = String.format("%d%%", (int) (recommendedArticle.getSimilarity() * 100));

                    return new RecommendArticleDto(
                            article.getId(),
                            article.getTitle(),
                            article.getCategory(),
                            article.getImage(),
                            article.getPublisher(),
                            article.getArticleTime(),
                            article.getCreatedDate(),
                            similarityPercentage
                    );
                })
                .collect(Collectors.toList());
    }

    private FlaskResponse fetchFlaskResponse(String url) {
        try {
            ResponseEntity<FlaskResponse> responseEntity = restTemplate.getForEntity(URI.create(url), FlaskResponse.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                log.error("GET 요청 실패: {}", responseEntity.getStatusCodeValue());
                return null;
            }
        } catch (Exception e) {
            log.error("GET 요청 실패:", e);
            return null;
        }
    }


    public List<UUIDArticleListDto> findClosestToTargetTimeByCategories(String[] categories, LocalTime targetTime, int pageSize) {
        // 해당 카테고리에 해당하는 모든 기사 가져오기
        List<UUIDArticle> allArticles = uuidArticleRepository.findByCategoryInOrderByArticleTimeAsc(categories);

        // 모든 기사를 무작위로 섞음
        Collections.shuffle(allArticles);

        // 타겟 시간에 가장 가까운 기사 선택
        List<UUIDArticle> recommendedArticles = new ArrayList<>();
        int totalSeconds = 0;
        for (UUIDArticle article : allArticles) {
            int articleSeconds = article.getArticleTime().toSecondOfDay();

            if (totalSeconds + articleSeconds <= targetTime.toSecondOfDay() && recommendedArticles.size() < pageSize) {
                recommendedArticles.add(article);
                totalSeconds += articleSeconds;
            } else {
                break;
            }
        }

        // 선택된 기사를 DTO로 변환하여 반환
        return entityToListDto(recommendedArticles, pageSize);
    }
}

