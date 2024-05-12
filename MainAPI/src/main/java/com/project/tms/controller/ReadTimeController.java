package com.project.tms.controller;

import com.project.tms.domain.ReadTime;
import com.project.tms.dto.ReadTimeCategoryDto;
import com.project.tms.repository.ReadTimeRepository;
import com.project.tms.service.ReadTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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

//    @GetMapping("/categoryReadTime/{memberId}")
//    public ResponseEntity<Object> getReadTimeByMemberId(@PathVariable Long memberId) {
//        ReadTimeCategoryDto readTimeCategoryDto = readTimeService.getReadTimeByMemberId(memberId);
//        return new ResponseEntity<>(readTimeCategoryDto, HttpStatus.OK);
//    }

    @GetMapping("/categoryReadTime/{memberId}")
    public ResponseEntity<Map<String, Object>> getReadTimeByMemberId(@PathVariable Long memberId) {
        Map<String, Object> responseBody = new HashMap<>();

        // 해당 memberId의 ReadTime 엔티티를 가져옵니다.
        List<ReadTime> readTimes = readTimeService.getReadTimesByMemberId(memberId);

        // 각 카테고리에 대한 읽은 시간을 responseBody에 추가합니다.
        List<Map<String, Object>> categories = new ArrayList<>();
        for (ReadTime readTime : readTimes) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("category", readTime.getCategory());
            categoryMap.put("time", readTime.getCategory() != null ? readTime.getCategoryValue() : null);
            categories.add(categoryMap);
        }

        // memberId 정보를 추가합니다.
        responseBody.put("memberId", memberId);
        responseBody.put("data", categories);

        return ResponseEntity.ok(responseBody);
    }


    @GetMapping("/topCategories/{memberId}")
    public ResponseEntity<Map<String, Object>> getTopCategoriesPercentage(@PathVariable Long memberId) {
        Map<String, Object> responseBody = new HashMap<>();

        // 해당 memberId의 ReadTime 엔티티를 가져옵니다.
        List<ReadTime> readTimes = readTimeService.getReadTimesByMemberId(memberId);

        // 각 카테고리의 총 읽은 시간을 계산합니다.
        Map<String, Long> categoryTotalTime = new HashMap<>();
        for (ReadTime readTime : readTimes) {
            String category = readTime.getCategory();
            Long totalTime = categoryTotalTime.getOrDefault(category, 0L);
            switch (category) {
                case "culture":
                    totalTime += readTime.getCulture().toSecondOfDay();
                    break;
                case "economy":
                    totalTime += readTime.getEconomy().toSecondOfDay();
                    break;
                case "entertain":
                    totalTime += readTime.getEntertain().toSecondOfDay();
                    break;
                case "politics":
                    totalTime += readTime.getPolitics().toSecondOfDay();
                    break;
                case "science":
                    totalTime += readTime.getScience().toSecondOfDay();
                    break;
                case "society":
                    totalTime += readTime.getSociety().toSecondOfDay();
                    break;
                case "sports":
                    totalTime += readTime.getSports().toSecondOfDay();
                    break;
                case "technology":
                    totalTime += readTime.getTechnology().toSecondOfDay();
                    break;
                case "world":
                    totalTime += readTime.getWorld().toSecondOfDay();
                    break;
            }
            categoryTotalTime.put(category, totalTime);
        }

        // 총 읽은 시간을 기준으로 백분율을 계산합니다.
        long totalReadTime = categoryTotalTime.values().stream().mapToLong(Long::longValue).sum();
        Map<String, Integer> categoryPercentages = new HashMap<>();
        for (Map.Entry<String, Long> entry : categoryTotalTime.entrySet()) {
            int percentage = (int) Math.round((double) entry.getValue() / totalReadTime * 100.0);
            categoryPercentages.put(entry.getKey(), percentage);
        }

        // 백분율이 높은 상위 4개의 카테고리를 선택합니다.
        List<Map.Entry<String, Integer>> sortedPercentages = categoryPercentages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(4)
                .collect(Collectors.toList());

        // 결과를 responseBody에 추가합니다.
        List<Map<String, Object>> topCategories = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedPercentages) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("category", entry.getKey());
            categoryMap.put("percentage", entry.getValue());
            topCategories.add(categoryMap);
        }

        // memberId 정보를 추가합니다.
        responseBody.put("memberId", memberId);
        responseBody.put("topCategories", topCategories);

        return ResponseEntity.ok(responseBody);
    }



//    @PostMapping("/readArticle")
//    public ResponseEntity<String> saveReadArticle(@RequestBody ReadArticleDto readArticleDto) {
//        try {
//            readTimeService.saveReadArticle(readArticleDto);
//            return ResponseEntity.ok("읽은 기사 uuid 저장 완료");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("읽은 기사 uuid 저장 실패");
//        }
//    }

//    @GetMapping("/readArticle/{memberId}")
//    public ResponseEntity<List<String>> getUuidArticleIdsByMemberId(@PathVariable Long memberId) {
//        List<String> uuidArticleIds = readTimeService.getUuidArticleIdsByMemberId(memberId);
//        return ResponseEntity.ok(uuidArticleIds);
//    }

}
