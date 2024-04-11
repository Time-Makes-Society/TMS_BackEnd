package com.project.tms;

import com.project.tms.dto.MemberDto;
import com.project.tms.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        createTestMember();
    }

    private void createTestMember() {

        MemberDto memberDto = new MemberDto();
        memberDto.setLoginId("test");
        memberDto.setPassword("test!");
        memberDto.setMemberName("testUser");

        memberService.join(memberDto);
    }
}
