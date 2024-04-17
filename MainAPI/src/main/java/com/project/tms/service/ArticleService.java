package com.project.tms.service;

import com.project.tms.domain.Article;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.UUIDArticleDTO;
import com.project.tms.repository.ArticleRepository;
import com.project.tms.repository.UUIDArticleRepository;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    public Page<UUIDArticle> manyCategoryfindAll(String category, Pageable pageable) {
        return uuidArticleRepository.findByCategoryOrderByCreatedDateDesc(category, pageable);
    }

    // 가공한 news 테이블 데이터를 PageDTO 형식으로 재가공하는 메서드
    public Page<UUIDArticleDTO> entityToPageDTO(Page<UUIDArticle> articlePage) {
        return articlePage.map(uuidArticle -> {
            UUIDArticleDTO dto = new UUIDArticleDTO();
            dto.setId(uuidArticle.getId());
            dto.setTitle(uuidArticle.getTitle());
            dto.setCategory(uuidArticle.getCategory());
            dto.setImage(uuidArticle.getImage());
            dto.setArticleTime(uuidArticle.getArticleTime());

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
            LocalTime articleTime = LocalTime.of(minutes, seconds);
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
}

