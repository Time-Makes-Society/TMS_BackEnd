package com.project.tms.controller;

import com.project.tms.domain.Member;
import com.project.tms.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @PostMapping("/members/save")
    public void personSave(@RequestBody Member member) {
        memberRepository.save(member);
    }
}
