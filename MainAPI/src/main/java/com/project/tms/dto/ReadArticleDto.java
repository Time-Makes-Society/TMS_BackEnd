package com.project.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadArticleDto {

    private Long memberId;
    private String uuidArticleId;
}
