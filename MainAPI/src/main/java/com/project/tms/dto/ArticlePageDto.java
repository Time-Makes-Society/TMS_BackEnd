package com.project.tms.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageDto {
    private int pageNumber;
    private int pageMaxSize;
    private int pageCurrentSize;
    private int totalPageNumber;
    private long totalSize;
}
