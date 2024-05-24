package com.project.tms.controller;

import com.project.tms.domain.ReadTime;
import com.project.tms.service.ReadTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/categoryReadTime/{memberId}")
    public ResponseEntity<Map<String, Object>> getReadTimeByMemberId(@PathVariable Long memberId) {
        Map<String, Object> responseBody = new HashMap<>();

        List<ReadTime> readTimes = readTimeService.getReadTimesByMemberId(memberId);

        Set<String> allCategories = new HashSet<>(Arrays.asList("문화", "경제", "연예", "정치", "과학", "사회", "스포츠", "기술", "해외"));

        List<Map<String, Object>> categories = new ArrayList<>();
        for (String category : allCategories) {
            boolean found = false;
            for (ReadTime readTime : readTimes) {
                if (category.equals(readTime.getCategory())) {
                    Map<String, Object> categoryMap = new HashMap<>();
                    categoryMap.put("category", readTime.getCategory());
                    categoryMap.put("time", readTime.getCategoryValue());
                    categories.add(categoryMap);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("category", category);
                categoryMap.put("time", "00:00:00");
                categories.add(categoryMap);
            }
        }

        responseBody.put("memberId", memberId);
        responseBody.put("data", categories);

        return ResponseEntity.ok(responseBody);
    }


    @GetMapping("/topCategories/{memberId}")
    public ResponseEntity<Map<String, Object>> getTopCategoriesPercentage(@PathVariable Long memberId) {
        Map<String, Object> responseBody = new HashMap<>();

        List<ReadTime> readTimes = readTimeService.getReadTimesByMemberId(memberId);

        Map<String, Long> categoryTotalTime = new HashMap<>();
        for (ReadTime readTime : readTimes) {
            String category = readTime.getCategory();
            long totalTime = 0;
            switch (category) {
                case "문화":
                    totalTime = readTime.getCulture().toSecondOfDay();
                    break;
                case "경제":
                    totalTime = readTime.getEconomy().toSecondOfDay();
                    break;
                case "연예":
                    totalTime = readTime.getEntertain().toSecondOfDay();
                    break;
                case "정치":
                    totalTime = readTime.getPolitics().toSecondOfDay();
                    break;
                case "과학":
                    totalTime = readTime.getScience().toSecondOfDay();
                    break;
                case "사회":
                    totalTime = readTime.getSociety().toSecondOfDay();
                    break;
                case "스포츠":
                    totalTime = readTime.getSports().toSecondOfDay();
                    break;
                case "기술":
                    totalTime = readTime.getTechnology().toSecondOfDay();
                    break;
                case "해외":
                    totalTime = readTime.getWorld().toSecondOfDay();
                    break;
            }
            categoryTotalTime.put(category, totalTime);
        }

        long totalReadTime = categoryTotalTime.values().stream().mapToLong(Long::longValue).sum();
        Map<String, Integer> categoryPercentages = new HashMap<>();
        for (Map.Entry<String, Long> entry : categoryTotalTime.entrySet()) {
            int percentage = (int) Math.round((double) entry.getValue() / totalReadTime * 100.0);
            categoryPercentages.put(entry.getKey(), percentage);
        }

        List<Map.Entry<String, Integer>> sortedPercentages = categoryPercentages.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(4)
                .collect(Collectors.toList());

        List<Map<String, Object>> topCategories = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedPercentages) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("category", entry.getKey());
            categoryMap.put("percentage", entry.getValue());
            topCategories.add(categoryMap);
        }

        while (topCategories.size() < 4) {
            Map<String, Object> nullCategoryMap = new HashMap<>();
            nullCategoryMap.put("category", "null");
            nullCategoryMap.put("percentage", 0);
            topCategories.add(nullCategoryMap);
        }

        responseBody.put("memberId", memberId);
        responseBody.put("topCategories", topCategories);

        return ResponseEntity.ok(responseBody);
    }

}
