package com.project.tms.controller;


import com.project.tms.client.GptClient;
import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.ChatResponse;
import com.project.tms.dto.gpt.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromptController {

    private final GptClient gpt;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody String prompt) {
        try {
            // ChatRequest 객체 생성
            ChatRequest request = new ChatRequest();
            request.setModel("gpt-3.5-turbo");

            // 사용자의 입력 메시지를 ChatRequest에 추가
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("user", prompt));
            request.setMessages(messages);

            // 기타 옵션들을 설정 (필요시)
            /*request.setTemperature(1);
            request.setMax_tokens(256);
            request.setTop_p(1);
            request.setFrequency_penalty(0);
            request.setPresence_penalty(0);*/

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
