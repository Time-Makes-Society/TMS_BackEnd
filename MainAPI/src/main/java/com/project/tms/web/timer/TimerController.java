package com.project.tms.web.timer;

import com.project.tms.dto.UserSetTimeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimerController {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

//    @PostMapping("/setTimer")
//    public ResponseEntity<String> setTimer(@RequestBody UserSetTimeDTO userSetTimeDTO) {
//        int userSetTime = userSetTimeDTO.getUserSetTime();
//        System.out.println("사용자가 설정한 시간: " + userSetTime + "초");
//
//        // 응답 보내기
//        return ResponseEntity.ok("타이머 설정이 완료되었습니다.");
//    }

    @PostMapping("/setTimer")
    public ResponseEntity<String> setTimer(@RequestBody UserSetTimeDTO userSetTimeDTO) {
        int userSetTime = userSetTimeDTO.getUserSetTime();
        log.info("타이머 시작");
        System.out.println("사용자가 설정한 시간: " + userSetTime + "초");

        executorService.schedule(() -> {
//            sendTimerExpiredRequest(); // 단순 "타이머 만료" 메시지
            sendTimerExpiredRequestLogout(); // 타이머 종료 → 사용자 로그아웃
        }, userSetTime, TimeUnit.SECONDS);

        // 응답 보내기
        return ResponseEntity.ok("타이머 설정이 완료되었습니다.");
    }

    private void sendTimerExpiredRequest() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/api/timerExpired", String.class);
        System.out.println("타이머 만료 요청 응답: " + response.getBody());
    }
    private void sendTimerExpiredRequestLogout() {

        log.info("타이머 만료");
        logoutUser();
    }

//    @GetMapping("/timerExpired")
//    public ResponseEntity<String> notifyTimerExpired() {
//        String message = "타이머 종료";
//        return ResponseEntity.ok(message);
//    }

    @GetMapping("/timerExpired")
    public ResponseEntity<String> notifyTimerExpired() {
        log.info("타이머 종료");
        String message = "타이머 종료";
        return ResponseEntity.ok(message);
    }

    private void logoutUser() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/logout", null, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("사용자 로그아웃 성공");
        } else {
            log.error("사용자 로그아웃 실패");
        }
    }
}
