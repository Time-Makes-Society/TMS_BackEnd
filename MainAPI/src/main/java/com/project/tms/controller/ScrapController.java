package com.project.tms.controller;

import com.project.tms.service.ScrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;


        @PostMapping("/scrap")
        public ResponseEntity<String> scrapArticle(@RequestParam Long memberId, @RequestParam UUID articleId) {
            log.info("스크랩 실행");
            scrapService.scrapArticle(memberId, articleId);
            return ResponseEntity.ok("스크랩 성공");
        }


    @PostMapping("/scrap/cancel")
    public ResponseEntity<String> scrapCancel(@RequestParam Long memberId, @RequestParam UUID articleId) {
        log.info("스크랩 취소 실행");
        scrapService.scrapCancel(memberId, articleId);
        return ResponseEntity.ok("스크랩 취소 완료");
    }
}
