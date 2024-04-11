package com.project.tms.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
}