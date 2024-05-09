package com.project.tms.service;

import com.project.tms.domain.Member;
import com.project.tms.domain.ReadTime;
import com.project.tms.dto.ReadTimeCategoryDto;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.ReadTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.QueryModifiers.limit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadTimeService {

    private final ReadTimeRepository readTimeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addReadTimeToCategory(Long memberId, String category, LocalTime readTime) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다."));

        // 해당 카테고리에 대한 읽은 시간을 검색
        ReadTime existingReadTime = readTimeRepository.findByMemberIdAndCategory(memberId, category);

        if (existingReadTime != null) {
            // 이미 해당 카테고리에 대한 읽은 시간이 존재하면 업데이트
            switch (category) {
                case "culture":
                    existingReadTime.setCulture(existingReadTime.getCulture().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "economy":
                    existingReadTime.setEconomy(existingReadTime.getEconomy().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "entertain":
                    existingReadTime.setEntertain(existingReadTime.getEntertain().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "politics":
                    existingReadTime.setPolitics(existingReadTime.getPolitics().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "science":
                    existingReadTime.setScience(existingReadTime.getScience().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "society":
                    existingReadTime.setSociety(existingReadTime.getSociety().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "sports":
                    existingReadTime.setSports(existingReadTime.getSports().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "technology":
                    existingReadTime.setTechnology(existingReadTime.getTechnology().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "world":
                    existingReadTime.setWorld(existingReadTime.getWorld().plusSeconds(readTime.toSecondOfDay()));
                    break;
                default:
                    throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + category);
            }
            readTimeRepository.save(existingReadTime);
        } else {
            // 해당 카테고리에 대한 데이터가 존재하지 않으면 새로운 데이터 생성
            ReadTime newReadTime = new ReadTime();
            newReadTime.setMember(member);
            newReadTime.setCategory(category);
            switch (category) {
                case "culture":
                    newReadTime.setCulture(readTime);
                    break;
                case "economy":
                    newReadTime.setEconomy(readTime);
                    break;
                case "entertain":
                    newReadTime.setEntertain(readTime);
                    break;
                case "politics":
                    newReadTime.setPolitics(readTime);
                    break;
                case "science":
                    newReadTime.setScience(readTime);
                    break;
                case "society":
                    newReadTime.setSociety(readTime);
                    break;
                case "sports":
                    newReadTime.setSports(readTime);
                    break;
                case "technology":
                    newReadTime.setTechnology(readTime);
                    break;
                case "world":
                    newReadTime.setWorld(readTime);
                    break;
                default:
                    throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + category);
            }
            readTimeRepository.save(newReadTime);
        }
    }

    public ReadTimeCategoryDto getReadTimeByMemberId(Long memberId) {
        ReadTimeCategoryDto readTimeCategoryDto = new ReadTimeCategoryDto();
        List<ReadTime> readTimes = readTimeRepository.findByMemberId(memberId);

        for (ReadTime readTime : readTimes) {
            if (readTime.getCategory().equals("culture")) {
                readTimeCategoryDto.setCulture(readTime.getCulture());
            } else if (readTime.getCategory().equals("economy")) {
                readTimeCategoryDto.setEconomy(readTime.getEconomy());
            } else if (readTime.getCategory().equals("entertain")) {
                readTimeCategoryDto.setEntertain(readTime.getEntertain());
            } else if (readTime.getCategory().equals("politics")) {
                readTimeCategoryDto.setPolitics(readTime.getPolitics());
            } else if (readTime.getCategory().equals("science")) {
                readTimeCategoryDto.setScience(readTime.getScience());
            } else if (readTime.getCategory().equals("society")) {
                readTimeCategoryDto.setSociety(readTime.getSociety());
            } else if (readTime.getCategory().equals("sports")) {
                readTimeCategoryDto.setSports(readTime.getSports());
            } else if (readTime.getCategory().equals("technology")) {
                readTimeCategoryDto.setTechnology(readTime.getTechnology());
            } else if (readTime.getCategory().equals("world")) {
                readTimeCategoryDto.setWorld(readTime.getWorld());
            }
        }

        readTimeCategoryDto.setMemberId(memberId);
        return readTimeCategoryDto;
    }
}