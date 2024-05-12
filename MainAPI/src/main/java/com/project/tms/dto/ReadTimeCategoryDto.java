package com.project.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadTimeCategoryDto {

    private Long memberId;

    private LocalTime culture;
    private LocalTime economy;
    private LocalTime entertain;
    private LocalTime politics;
    private LocalTime science;
    private LocalTime society;
    private LocalTime sports;
    private LocalTime technology;
    private LocalTime world;


    private Map<String, LocalTime> categoryTimes = new HashMap<>();

    public void addCategoryTime(String category, LocalTime time) {
        categoryTimes.put(category, time);
    }

}
