//package com.project.tms.web.timer;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//@RequiredArgsConstructor
//@RequestMapping("/api")
//public class TimerTask {
//
////    private final HttpSession session;
////    private final RestTemplate restTemplate;
//    private final TimerController timerController; // TimerController 주입
//
//    public void handleTimerExpiration() {
//        ResponseEntity<String> responseEntity = timerController.notifyTimerExpired(); // TimerController의 메서드 호출
//        String message = responseEntity.getBody();
//        System.out.println("Timer expired notification: " + message);
//    }
//}
