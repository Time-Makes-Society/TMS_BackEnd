package com.project.tms.dto;

import com.project.tms.domain.UUIDArticle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapDto {

    private String uuidArticleId;
    private String title;
    private String category;
    private String content;
    private LocalDateTime createdDate;
    private String image;
    private String link;
    private LocalTime articleTime;

}
