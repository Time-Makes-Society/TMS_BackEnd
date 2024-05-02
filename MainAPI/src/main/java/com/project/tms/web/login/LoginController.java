package com.project.tms.web.login;

import com.project.tms.domain.Member;
import com.project.tms.dto.MemberDto;
import com.project.tms.dto.MemberLoginDto;
import com.project.tms.service.LoginService;
import com.project.tms.web.LoginCheckFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

//    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody MemberDto memberDto, HttpSession session) {


        log.info("로그인 실행");
        Member loginMember = loginService.login(memberDto.getLoginId(), memberDto.getPassword());

        if (loginMember == null) {
            log.info("로그인 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("아이디 또는 비밀번호가 맞지 않습니다.");
        }

        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        log.info("로그인 성공");
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginV2(@Valid @RequestBody MemberLoginDto memberDto, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("로그인 실행");
            Member loginMember = loginService.login(memberDto.getLoginId(), memberDto.getPassword());

            if (loginMember == null) {
                log.info("로그인 실패");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("아이디 또는 비밀번호가 맞지 않습니다.");
            }

            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            log.info("로그인 성공");

            return ResponseEntity.ok("로그인 성공");
        } catch (Exception e) {
            log.error("로그인 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("로그인 중 오류가 발생했습니다.");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        log.info("로그아웃 성공");
        return ResponseEntity.ok("로그아웃 성공");
    }

}
