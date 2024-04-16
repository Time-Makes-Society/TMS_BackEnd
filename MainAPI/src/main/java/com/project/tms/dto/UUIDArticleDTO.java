package com.project.tms.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class UUIDArticleDTO {

    private UUID id;
    private String title;
    private LocalDateTime createdDate;
    private String category;
    private String image;
    private LocalTime articleTime;

}

