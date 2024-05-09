package com.project.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, LocalTime> toMap() {
        Map<String, LocalTime> categoryTimes = new HashMap<>();
        if (culture != null) categoryTimes.put("culture", culture);
        if (economy != null) categoryTimes.put("economy", economy);
        if (entertain != null) categoryTimes.put("entertain", entertain);
        if (politics != null) categoryTimes.put("politics", politics);
        if (science != null) categoryTimes.put("science", science);
        if (society != null) categoryTimes.put("society", society);
        if (sports != null) categoryTimes.put("sports", sports);
        if (technology != null) categoryTimes.put("technology", technology);
        if (world != null) categoryTimes.put("world", world);
        return categoryTimes;
    }
}
