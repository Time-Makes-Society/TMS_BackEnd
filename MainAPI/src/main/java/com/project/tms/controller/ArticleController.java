package com.project.tms.controller;

import com.project.tms.domain.Article;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.repository.ArticleRepository;
import com.project.tms.repository.UUIDArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UUIDArticleRepository uuidArticleRepository;

    // 플라스크 전처리 서버에 HTTP GET 요청 보내는 메서드
    private void sendGetRequest(String url) {
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
                System.out.println("플라스크 서버 응답: " + response.toString());
            } else {
                System.out.println("GET 요청 실패: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // flaks 전처리 서버를 작동시켜 pre_news를 데이터를 UUID가 붙은 news 데이터로 저장하는 메서드
    @GetMapping("/{category}")
    public ResponseEntity<String> refreshAndConvertToUUID(@PathVariable(name = "category") String category) throws UnsupportedEncodingException {
        // http://localhost:8081/newssave/category로 엔트포인트로 GET 요청을 보냄
        String encodedCategory = URLEncoder.encode(category, StandardCharsets.UTF_8);
        String flaksServerUrl = "http://localhost:8081/newssave/" + encodedCategory;
        sendGetRequest(flaksServerUrl);

        List<Article> articles = articleRepository.findAll();

        // 데이터를 읽어와서 새로운 엔티티를 생성하고 저장
        processDataAndSaveAsNewEntity(articles);

        return ResponseEntity.ok("저장이 완료되었습니다.");
    }


    // 데이터를 읽어와서 새로운 엔티티를 생성하고 저장하는 메서드
    private void processDataAndSaveAsNewEntity(List<Article> articles) {
        for (Article article : articles) {
            // 기존 Article 엔티티에서 필요한 데이터를 가져와서 UUIDArticle을 생성
            UUIDArticle uuidArticle = new UUIDArticle();
            uuidArticle.setTitle(article.getTitle());
            uuidArticle.setContent(article.getContent());
            uuidArticle.setCreatedDate(article.getCreatedDate());
            uuidArticle.setCategory(article.getCategory());
            uuidArticle.setImage(article.getImage());
            uuidArticle.setLink(article.getLink());

            // 새로운 엔티티 저장
            uuidArticleRepository.save(uuidArticle);
        }
    }

    // UUID이 붙은 Article 데이터를 가져오는 메서드
    @GetMapping("/uuid")
    public ResponseEntity<List<UUIDArticle>> getAllUUIDArticles() {
        List<UUIDArticle> uuidArticles = uuidArticleRepository.findAll();
        return ResponseEntity.ok(uuidArticles);
    }

    // ID가 1인 기사를 가져오는 엔드포인트
    @GetMapping("/id1")
    public ResponseEntity<Article> getArticleById1() {
        Optional<Article> optionalArticle = articleRepository.findById(1L); // ID가 1인 기사를 가져옴
        return optionalArticle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
