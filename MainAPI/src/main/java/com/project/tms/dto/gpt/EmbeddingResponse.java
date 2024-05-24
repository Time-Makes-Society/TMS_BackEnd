package com.project.tms.dto.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmbeddingResponse {

    @JsonProperty("data")
    private List<DataItem> data;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DataItem {

        @JsonProperty("embedding")
        private List<Double> embedding;
    }
}
