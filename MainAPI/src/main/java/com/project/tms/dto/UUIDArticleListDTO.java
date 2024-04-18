package com.project.tms.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class UUIDArticleListDTO {

    private UUID id;
    private String title;
    private String content;
    private String category;
    private String image;
    private String publisher;
    private LocalTime articleTime;
    private LocalDateTime createdDate;

}

