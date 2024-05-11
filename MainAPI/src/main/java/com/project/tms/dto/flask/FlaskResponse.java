package com.project.tms.dto.flask;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlaskResponse {

    @JsonProperty("recommendedArticles")
    private List<RecommendedArticle> recommendedArticles;

}
