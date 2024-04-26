package com.project.tms.client;

import com.project.tms.dto.gpt.ChatRequest;
import com.project.tms.dto.gpt.ChatResponse;

public interface GptClient {
    ChatResponse chat(ChatRequest request);
}
