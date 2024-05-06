package com.project.tms.controller;

import com.project.tms.domain.ReadTime;
import com.project.tms.dto.ReadTimeCategoryDto;
import com.project.tms.service.MemberService;
import com.project.tms.service.ReadTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ReadTimeController {

    private final ReadTimeService readTimeService;

    @PostMapping("/categoryReadTime")
    public ResponseEntity<Object> addReadTimeToCategory(@RequestBody ReadTime readTime) {
        try {
            readTimeService.addReadTimeToCategory(readTime.getMember().getId(), readTime.getCategory(), readTime.getReadTime());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.OK.value());
            responseBody.put("message", "읽은 시간이 카테고리에 추가되었습니다.");

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "읽은 시간을 추가하는 중에 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/readTime/{memberId}")
    public ResponseEntity<ReadTimeCategoryDto> getReadTimeByMemberId(@PathVariable Long memberId) {
        ReadTimeCategoryDto readTimeCategoryDto = readTimeService.getReadTimeByMemberId(memberId);
        return new ResponseEntity<>(readTimeCategoryDto, HttpStatus.OK);
    }
}
