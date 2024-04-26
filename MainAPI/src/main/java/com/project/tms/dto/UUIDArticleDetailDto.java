package com.project.tms.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class UUIDArticleDetailDto {
    private String title;
    private String content;
    private String category;
    private String image;
    private String link;
    private LocalTime articleTime;
    private LocalDateTime createdDate;
}
