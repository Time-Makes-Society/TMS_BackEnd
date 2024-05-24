package com.project.tms.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String memberNickname;
}



