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

import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @GetMapping("/categoryReadTime/{memberId}")
    public ResponseEntity<ReadTimeCategoryDto> getReadTimeByMemberId(@PathVariable Long memberId) {
        ReadTimeCategoryDto readTimeCategoryDto = readTimeService.getReadTimeByMemberId(memberId);
        return new ResponseEntity<>(readTimeCategoryDto, HttpStatus.OK);
    }

    @GetMapping("/readTimeRank/{memberId}")
    public ResponseEntity<Map<String, Integer>> getTopCategoriesPercentage(@PathVariable Long memberId) {
        ReadTimeCategoryDto readTimeCategoryDto = readTimeService.getReadTimeByMemberId(memberId);
        Map<String, LocalTime> categoryTimes = readTimeCategoryDto.toMap();

        List<Map.Entry<String, LocalTime>> nonNullCategories = categoryTimes.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        double totalTime = nonNullCategories.stream()
                .mapToLong(entry -> entry.getValue().toSecondOfDay())
                .sum();

        Map<String, Integer> categoryPercentages = new HashMap<>();
        nonNullCategories.stream().limit(4).forEach(entry -> {
            int percentage = (int) (entry.getValue().toSecondOfDay() / totalTime * 100.0);
            categoryPercentages.put(entry.getKey(), percentage);
        });

        return new ResponseEntity<>(categoryPercentages, HttpStatus.OK);
    }

}
