package com.project.tms.controller;


import com.project.tms.client.GptClient;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.UUIDArticleDetailDto;
import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.ChatResponse;
import com.project.tms.dto.gpt.EmbeddingRequest;
import com.project.tms.dto.gpt.EmbeddingResponse;
import com.project.tms.service.ArticleService;
import com.project.tms.service.GptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    private final ArticleService articleService;

    private final GptClient gpt;

    @GetMapping("/summarize/{uuid}")
    public ResponseEntity<Object> chat(@PathVariable(name = "uuid") String uuidString) {
        try {
            UUID uuid = UUID.fromString(uuidString);

            // uuid에 맞는 기사를 검색해서 내용만 추출함
            String articleContent = gptService.articleFindOneToConvertContent(uuid);

            // 내용을 요약해 주는 프롬프트 엔지니어링을 거침
            ChatRequest request = gptService.startEngineerPrompt(articleContent);

            // GptClient를 통해 chat 요청을 보내고 응답을 받음
            ChatResponse response = gpt.chat(request);

            // 요약된 내용을 Choice 객체에서 가져와서 content 필드에 설정
            String summarizedContent = response.getChoices().get(0).getMessage().getContent();

            UUIDArticle uuidArticle = articleService.articleFindOne(uuid).orElse(null);

            uuidArticle.setContent(summarizedContent);

            UUIDArticleDetailDto uuidArticleDetailDto = articleService.entityToDetailDto(uuidArticle);

            return ResponseEntity.ok(uuidArticleDetailDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유효하지 않은 기사 id입니다.");
        }
    }

    // 임베딩 테스트용
    @GetMapping("/embedding/{uuid}")
    public ResponseEntity<String> calculateAndSaveEmbedding(@PathVariable UUID uuid) {
        try {
            String articleTitle = gptService.articleFindOneToConvertTitle(uuid);

            // 기사 제목을 사용하여 임베딩 요청 생성
            EmbeddingRequest request = gptService.calculateEmbeddingRequest(articleTitle);

            // 생성된 임베딩 요청을 사용하여 임베딩 값을 요청
            EmbeddingResponse response = gpt.embedding(request);

            UUIDArticle uuidArticle = new UUIDArticle();
            String embedding = response.getData().get(0).getEmbedding().toString();
            articleService.saveEmbedding(uuidArticle, embedding);

            // 응답 반환
            return ResponseEntity.ok().body("임베딩 값이 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("임베딩 값을 저장하는 중에 오류가 발생했습니다.");
        }
    }

}
