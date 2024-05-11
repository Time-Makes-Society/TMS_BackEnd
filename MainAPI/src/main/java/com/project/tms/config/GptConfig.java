/*
package com.project.tms.config;

import com.project.tms.client.GptClient;
import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GptConfig implements GptClient {

    private final WebClient webClient;

    public GptConfig(@Value("${api-key:}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        return webClient.post()
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();
    }
}
*/

package com.project.tms.config;

import com.project.tms.client.GptClient;
import com.project.tms.dto.gpt.EmbeddingRequest;
import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.ChatResponse;
import com.project.tms.dto.gpt.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GptConfig implements GptClient {

    private final WebClient webClient;

    public GptConfig(@Value("${api-key:}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        return webClient.post()
                .uri("/chat/completions")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();
    }

    @Override
    public EmbeddingResponse embedding(EmbeddingRequest request) {
        // OpenAI API를 통해 Embedding 값을 가져오는 요청을 보냄
        return webClient.post() // POST 요청으로 변경
                .uri("/embeddings")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .block();
    }


}
