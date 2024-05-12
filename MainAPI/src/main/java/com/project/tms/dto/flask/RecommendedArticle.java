package com.project.tms.dto.flask;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class RecommendedArticle {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("similarity")
    private double similarity;

    @JsonProperty("title")
    private String title;
}
