package com.project.tms.web.timer;

import com.project.tms.dto.UserSetTimeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimerController {

    @PostMapping("/setTimer")
    public ResponseEntity<String> setTimer(@RequestBody UserSetTimeDTO userSetTimeDTO) {
        int userSetTime = userSetTimeDTO.getUserSetTime();
        System.out.println("사용자가 설정한 시간: " + userSetTime + "초");

        // 응답 보내기
        return ResponseEntity.ok("타이머 설정이 완료되었습니다.");
    }
}
