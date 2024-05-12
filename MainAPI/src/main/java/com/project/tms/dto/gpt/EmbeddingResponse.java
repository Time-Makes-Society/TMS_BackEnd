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

    /*public List<Double> getEmbedding() {
        if (data != null && !data.isEmpty()) {
            DataItem dataItem = data.get(0);
            return dataItem != null ? dataItem.getEmbedding() : null;
        }
        return null;
    }*/

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DataItem {

        @JsonProperty("embedding")
        private List<Double> embedding;
    }
}
