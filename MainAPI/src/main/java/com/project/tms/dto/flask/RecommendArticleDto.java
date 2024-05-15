package com.project.tms.dto.flask;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendArticleDto {
    private UUID id;
    private String title;
    private String category;
    private String image;
    private String publisher;
    private LocalTime articleTime;
    private LocalDateTime createdDate;
    private String similarity;
}
