package com.project.tms.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @NotEmpty
    private String loginId; // 사용자 로그인 ID

    @NotEmpty
    private String password; // 비밀번호

    @NotEmpty
    private String memberName; // 사용자 이름

    @NotEmpty
    private String memberNickname; // 사용자 닉네임

//    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime totalReadTime;
}
