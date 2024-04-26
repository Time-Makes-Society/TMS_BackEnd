package com.project.tms.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String commentCreatedDate;
    private UUID articleId;
    private String articleTitle;
    private Long userId;
    private String memberName;
}



