package com.project.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadTimeDto {

    private Long memberId;
    private String readTime; // 시간을 문자열로 전달

}
