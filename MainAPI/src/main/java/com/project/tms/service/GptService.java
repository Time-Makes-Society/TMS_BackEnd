package com.project.tms.service;


import com.project.tms.client.GptClient;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.gpt.EmbeddingRequest;
import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.EmbeddingResponse;
import com.project.tms.dto.gpt.Message;
import com.project.tms.repository.UUIDArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class GptService {


    private final UUIDArticleRepository uuidArticleRepository;

    private final GptClient gpt;

    public String articleFindOneToConvertContent(UUID uuid) {

        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("기사를 찾지 못했습니다. " + uuid));


        String articleContent = article.getContent().toString();
        return articleContent;
    }

    // 프롬프트 엔지니어링 메서드
    public ChatRequest startEngineerPrompt(String articleContent) {
        // ChatRequest 객체 생성
        ChatRequest request = new ChatRequest();
        request.setModel("gpt-3.5-turbo");

        // 사용자의 입력 메시지와 시스템 어시스턴트를 ChatRequest에 추가
        List<Message> messages = new ArrayList<>();

        // 시스템 룰 추가
        messages.add(addSystemRule("너는 TMS라는 기사 요약 서비스의 챗봇이야."));
        messages.add(addSystemRule("|원문 기사 데이터: 기사 원문 내용| 형식으로 주어져."));
        messages.add(addSystemRule("따라서 기사 원문이 주어지면 너는 원문 기사 총 문자 길이의 70%% 이상 핵심 내용만 요약해 주어야해."));
        messages.add(addSystemRule("원본 데이터는 아무리 짧아도 무조건 적으로 핵심만 요약을 해주어야 해."));
        messages.add(addSystemRule("어투는 일정하게 대답해야 하며 ~있습니다.의 형식으로 대답해주어야 해"));

        // 사용자 쿼리 시작 지점 정의
        messages.add(new Message("user", startUserQuery(articleContent)));

        log.info("프롬프트 엔지니어링 내용: " + messages);

        request.setMessages(messages);

        return request;
    }


    // 시스템 룰을 추가하는 메서드
    private Message addSystemRule(String rule) {
        Message systemRule = new Message("system", rule);

        return systemRule;
    }

    // 사용자 쿼리 시작 지점 정의
    private String startUserQuery(String articleContent) {
        return "|원문 기사 데이터: " + articleContent + " |";
    }

    public String articleFindOneToConvertTitle(UUID uuid) {

        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("기사를 찾지 못했습니다. " + uuid));

        String articleTitle = article.getTitle().toString();
        return articleTitle;
    }

    public EmbeddingRequest calculateEmbeddingRequest(String articleTitle) {
        EmbeddingRequest request = new EmbeddingRequest();
        request.setInput(articleTitle); // 기사 제목을 입력으로 설정
        request.setModel("text-embedding-3-small"); // 사용할 임베딩 모델 지정
        return request;
    }


    public String calculateAndSaveEmbedding(UUIDArticle uuidArticle) {
        try {
            String articleTitle = articleFindOneToConvertTitle(uuidArticle.getId());

            // 기사 제목을 사용하여 임베딩 요청 생성
            EmbeddingRequest request = calculateEmbeddingRequest(articleTitle);

            // 생성된 임베딩 요청을 사용하여 임베딩 값을 요청
            EmbeddingResponse response = gpt.embedding(request);

            // 임베딩 값을 문자열로 변환하여 반환
            String embedding = response.getData().get(0).getEmbedding().toString();

            // UUIDArticle 엔티티에 임베딩 값 저장
            uuidArticle.setEmbedding(embedding);

            // UUIDArticle 엔티티 저장
            uuidArticleRepository.save(uuidArticle);

            return embedding;
        } catch (Exception e) {
            throw new RuntimeException("임베딩 값을 계산하고 저장하는 중에 오류가 발생했습니다.", e);
        }
    }

}
