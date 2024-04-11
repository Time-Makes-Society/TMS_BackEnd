package com.project.tms;

import com.project.tms.domain.Member;
import com.project.tms.repository.MemberRepository;
import com.project.tms.web.login.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final MemberRepository memberRepository;

    @GetMapping("/main")
    public ResponseEntity<String> getMain(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {

        if (loginMember == null) {
            return ResponseEntity.ok("main");
        } else {
            return ResponseEntity.ok("loginMain");
        }
    }
}
