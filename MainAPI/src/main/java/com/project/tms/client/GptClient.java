package com.project.tms.client;

import com.project.tms.dto.gpt.EmbeddingRequest;
import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.ChatResponse;
import com.project.tms.dto.gpt.EmbeddingResponse;

public interface GptClient {
    ChatResponse chat(ChatRequest request);

    EmbeddingResponse embedding(EmbeddingRequest request);
}
