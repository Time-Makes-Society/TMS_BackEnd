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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


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

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
                responseBody.put("message", "아이디 또는 비밀번호가 맞지 않습니다.");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(responseBody);
            }

            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            log.info("로그인 성공");

//            return ResponseEntity.ok("로그인 성공");

            // 로그인 성공 시 응답 생성
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.OK.value());
            responseBody.put("data", loginMember); // 로그인 멤버의 정보를 넣어줍니다.
            responseBody.put("message", "로그인 성공");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("로그인 중 오류 발생", e);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseBody.put("data", null);
            responseBody.put("message", "로그인 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        log.info("로그아웃 성공");
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/timerout")
    public ResponseEntity<String> logout() {
        // 로그아웃되었다는 메시지를 응답으로 보냅니다.

        log.info("타이머 만료 로그아웃 메서드 실행 성공");
        return ResponseEntity.status(HttpStatus.OK)
                .body("타이머 종료로 로그아웃되었습니다.");
    }
}
