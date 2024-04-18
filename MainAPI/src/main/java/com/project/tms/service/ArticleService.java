package com.project.tms.service;

import com.project.tms.domain.Article;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.UUIDArticleDetailDTO;
import com.project.tms.dto.UUIDArticleListDTO;
import com.project.tms.repository.ArticleRepository;
import com.project.tms.repository.UUIDArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArticleService {


    private final UUIDArticleRepository uuidArticleRepository;

    private final ArticleRepository articleRepository;


    // 모든 pre_news에 있는 데이터를 가져오는 메서드
    public List<Article> preNewsDBFindAll() {
        return articleRepository.findAll();
    }

    // 모든 가공한 news 테이블 데이터를 가져오는 메서드
    public Page<UUIDArticle> noCategoryFindAll(Pageable pageable) {
        return uuidArticleRepository.findAll(pageable);
    }

    public Page<UUIDArticle> manyCategoryFindAll(String category, Pageable pageable) {
        return uuidArticleRepository.findByCategoryOrderByCreatedDateDesc(category, pageable);
    }


    public UUIDArticleDetailDTO articleFindOne(UUID uuid) {
        UUIDArticle uuidArticle = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("UUIDArticle not found with id: " + uuid));

        return entityToDetailDTO(uuidArticle);
    }

    private UUIDArticleDetailDTO entityToDetailDTO(UUIDArticle uuidArticle) {
        UUIDArticleDetailDTO articleDetailDTO = new UUIDArticleDetailDTO();

        articleDetailDTO.setTitle(uuidArticle.getTitle());
        articleDetailDTO.setContent(uuidArticle.getContent());
        articleDetailDTO.setCategory(uuidArticle.getCategory());
        articleDetailDTO.setImage(uuidArticle.getImage());
        articleDetailDTO.setLink(uuidArticle.getLink());
        articleDetailDTO.setArticleTime(uuidArticle.getArticleTime());

        // 시간대 변환
        LocalDateTime createdDate = uuidArticle.getCreatedDate().minusHours(9); // UTC 시간에서 9시간을 빼서 한국 시간대로 변환
        articleDetailDTO.setCreatedDate(createdDate);

        return articleDetailDTO;
    }


    // 가공한 news 테이블 데이터를 PageDTO 형식으로 재가공하는 메서드
    public Page<UUIDArticleListDTO> entityToPageDTO(Page<UUIDArticle> articlePage) {
        return articlePage.map(uuidArticle -> {
            UUIDArticleListDTO dto = new UUIDArticleListDTO();
            dto.setId(uuidArticle.getId());
            dto.setTitle(uuidArticle.getTitle());
            dto.setCategory(uuidArticle.getCategory());
            dto.setImage(uuidArticle.getImage());
            dto.setArticleTime(uuidArticle.getArticleTime());
            dto.setPublisher(uuidArticle.getPublisher());

            // 시간대 변환
            LocalDateTime createdDate = uuidArticle.getCreatedDate().minusHours(9); // UTC 시간에서 9시간을 빼서 한국 시간대로 변환
            dto.setCreatedDate(createdDate);
            return dto;
        });
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

            // 새로운 엔티티 저장
            uuidArticleRepository.save(uuidArticle);
        }
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

    // UUIDArticle 엔티티를 UUIDArticleDTO로 변환하는 메서드
    private UUIDArticleListDTO entityToDTO(UUIDArticle uuidArticle) {
        UUIDArticleListDTO dto = new UUIDArticleListDTO();
        dto.setId(uuidArticle.getId());
        dto.setTitle(uuidArticle.getTitle());
        dto.setCategory(uuidArticle.getCategory());
        dto.setImage(uuidArticle.getImage());
        dto.setArticleTime(uuidArticle.getArticleTime());

        // 시간대 변환
        LocalDateTime createdDate = uuidArticle.getCreatedDate().minusHours(9); // UTC 시간에서 9시간을 빼서 한국 시간대로 변환
        dto.setCreatedDate(createdDate);
        return dto;
    }


    // 쿼리를 사용하여 target 시간에 가장 가까운 기사들을 가져오는 메서드
    public List<UUIDArticleListDTO> findClosestToTargetTimeByCategories(String[] categories, LocalTime targetTime, int pageSize) {
        List<UUIDArticle> closestArticles = new ArrayList<>();

        // 쿼리를 사용하여 각 카테고리에서 target 시간 이전의 기사들을 가져옴
        List<UUIDArticle> allArticles = uuidArticleRepository.findClosestArticlesByCategoriesAndTargetTime(List.of(categories), targetTime, Integer.MAX_VALUE);

        // 가장 가까운 기사들을 찾는 메서드 호출
        findClosestArticles(allArticles, targetTime, 0, LocalTime.of(0, 0, 0), new ArrayList<>(), closestArticles);

        // 페이지에 맞게 결과를 자름
        int startIndex = pageSize;
        int endIndex = Math.min(startIndex, closestArticles.size());

        // DTO로 변환하여 반환
        return closestArticles.subList(startIndex, endIndex)
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    // target 쿼리스트링 시간에 가장 가까운 기사들의 조합을 찾아 주는 재귀 메서드
    private void findClosestArticles(List<UUIDArticle> articles, LocalTime target, int index, LocalTime currentSum, List<UUIDArticle> selectedArticles, List<UUIDArticle> closestArticles) {
        if (index == articles.size()) {
            // 현재까지의 합의 시간과 목표 시간과의 차이를 계산
            long currentDifference = Math.abs(Duration.between(currentSum, target).getSeconds());
            // 현재까지 선택된 기사 리스트가 비어있거나 가장 가까운 시간과의 차이가 더 작은 경우 기사 리스트를 갱신
            if (closestArticles.isEmpty() || currentDifference < closestArticles.stream().mapToLong(article -> Math.abs(Duration.between(article.getArticleTime(), target).getSeconds())).min().orElse(Long.MAX_VALUE)) {
                closestArticles.clear();
                closestArticles.addAll(selectedArticles);
            }
            return;
        }

        // 현재 기사를 선택하지 않는 경우
        findClosestArticles(articles, target, index + 1, currentSum, selectedArticles, closestArticles);

        // 현재 기사를 선택하는 경우
        UUIDArticle currentArticle = articles.get(index);
        selectedArticles.add(currentArticle);

        // Duration을 사용하여 시간을 더함
        findClosestArticles(articles, target, index + 1, currentSum.plus(Duration.between(LocalTime.MIN, currentArticle.getArticleTime())), selectedArticles, closestArticles);
        selectedArticles.remove(currentArticle); // 선택한 기사를 다시 제거하여 백트래킹
    }


}

