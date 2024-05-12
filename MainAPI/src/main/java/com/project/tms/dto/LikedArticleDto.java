package com.project.tms.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class LikedArticleDto {

    private List<UUIDArticleListDto> likedArticleList;

}
