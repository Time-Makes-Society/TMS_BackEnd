package com.project.tms.controller;

import com.project.tms.domain.Member;
import com.project.tms.domain.Scrap;
import com.project.tms.dto.ScrapDto;
import com.project.tms.service.MemberService;
import com.project.tms.service.ScrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final MemberService memberService;


//    @PostMapping("/scrap")
//    public ResponseEntity<String> scrapArticle(@RequestParam Long memberId, @RequestParam UUID articleId) {
//        log.info("스크랩 실행");
//        scrapService.scrapArticle(memberId, articleId);
//        return ResponseEntity.ok("스크랩 성공");
//    }

    /**
     * json 형식으로 파라미터 전달받도록 수정
     */
    @PostMapping("/scrap")
    public ResponseEntity<String> scrapArticle(@RequestBody Map<String, Object> request) {
        Long memberId = Long.parseLong(request.get("memberId").toString());
        UUID articleId = UUID.fromString(request.get("articleId").toString());
        log.info("스크랩 실행");
        scrapService.scrapArticle(memberId, articleId);
        return ResponseEntity.ok("스크랩 성공");
    }


//    @PostMapping("/scrap/cancel")
//    public ResponseEntity<String> scrapCancel(@RequestParam Long memberId, @RequestParam UUID articleId) {
//        log.info("스크랩 취소 실행");
//        scrapService.scrapCancel(memberId, articleId);
//        return ResponseEntity.ok("스크랩 취소 완료");
//    }

    /**
     * json 형식 전달받도록 수정
     */
    @PostMapping("/scrap/cancel")
    public ResponseEntity<String> scrapCancel(@RequestBody Map<String, Object> request) {
        Long memberId = Long.parseLong(request.get("memberId").toString());
        UUID articleId = UUID.fromString(request.get("articleId").toString());
        log.info("스크랩 취소 실행");
        scrapService.scrapCancel(memberId, articleId);
        return ResponseEntity.ok("스크랩 취소 완료");
    }


    //    @GetMapping("/{memberId}/scrap")
    public ResponseEntity<List<String>> getScrapList(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        // memberId에 해당하는 멤버가 스크랩한 내용을 조회하여 리스트로 변환
        List<String> scrapList = scrapService.getScrapList(memberId);
        return ResponseEntity.ok(scrapList);
    }

    @GetMapping("/{memberId}/scrap")
    public ResponseEntity<List<ScrapDto>> getScrapDtoList(@PathVariable Long memberId, Long scrapId) {
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

//        ScrapDto scrapList = scrapService.getScrapListDto(memberId);
        List<ScrapDto> scrapListDto = scrapService.getScrapListDto(memberId);
        return ResponseEntity.ok(scrapListDto);
    }
}
