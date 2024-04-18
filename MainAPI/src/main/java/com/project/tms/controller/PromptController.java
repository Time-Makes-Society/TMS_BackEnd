package com.project.tms.controller;


import com.project.tms.client.GptClient;
import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.ChatResponse;
import com.project.tms.service.PromptEngineeringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromptController {

    @Autowired
    private final PromptEngineeringService promptEngineeringService;

    private final GptClient gpt;

    @GetMapping("/summarize/{uuid}")
    public ResponseEntity<ChatResponse> chat(@PathVariable(name = "uuid") UUID uuid) {
        try {
            // uuid에 맞는 기사를 검색해서 내용만 추출함
            String articleContent = promptEngineeringService.articleFindOneToConvertContent(uuid);

            // 내용을 요약해 주는 프롬프트 엔지니어링을 거침
            ChatRequest request = promptEngineeringService.startEngineerPrompt(articleContent);

            // GptClient를 통해 chat 요청을 보내고 응답을 받음
            ChatResponse response = gpt.chat(request);
            log.info("GPT response: {}", response.toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while processing chat request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
