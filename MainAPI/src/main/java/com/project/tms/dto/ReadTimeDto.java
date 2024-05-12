package com.project.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadTimeDto {
    private Long memberId;
    private String readTime; // 시간을 문자열로 전달

    // 문자열을 LocalTime으로 변환하는 메서드
//    public LocalTime getReadTimeAsLocalTime() {
//        return LocalTime.parse(readTime); // 문자열을 LocalTime으로 변환하여 반환
//    }
    public String getReadTimeAsString() {
        if (readTime != null) {
            return readTime.toString();
        }
        return null;
    }


    // LocalTime을 문자열로 변환하는 메서드
    public void setReadTimeFromLocalTime(LocalTime localTime) {
        this.readTime = localTime != null ? localTime.toString() : null;
    }
}
