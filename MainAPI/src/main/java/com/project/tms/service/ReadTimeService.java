package com.project.tms.service;


import com.project.tms.domain.Member;
import com.project.tms.domain.ReadTime;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.ReadTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

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
                case "문화":
                    existingReadTime.setCulture(existingReadTime.getCulture().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "경제":
                    existingReadTime.setEconomy(existingReadTime.getEconomy().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "연예":
                    existingReadTime.setEntertain(existingReadTime.getEntertain().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "정치":
                    existingReadTime.setPolitics(existingReadTime.getPolitics().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "과학":
                    existingReadTime.setScience(existingReadTime.getScience().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "사회":
                    existingReadTime.setSociety(existingReadTime.getSociety().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "스포츠":
                    existingReadTime.setSports(existingReadTime.getSports().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "기술":
                    existingReadTime.setTechnology(existingReadTime.getTechnology().plusSeconds(readTime.toSecondOfDay()));
                    break;
                case "해외":
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
                case "문화":
                    newReadTime.setCulture(readTime);
                    break;
                case "경제":
                    newReadTime.setEconomy(readTime);
                    break;
                case "연예":
                    newReadTime.setEntertain(readTime);
                    break;
                case "정치":
                    newReadTime.setPolitics(readTime);
                    break;
                case "과학":
                    newReadTime.setScience(readTime);
                    break;
                case "사회":
                    newReadTime.setSociety(readTime);
                    break;
                case "스포츠":
                    newReadTime.setSports(readTime);
                    break;
                case "기술":
                    newReadTime.setTechnology(readTime);
                    break;
                case "해외":
                    newReadTime.setWorld(readTime);
                    break;
                default:
                    throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + category);
            }
            readTimeRepository.save(newReadTime);
        }
    }


    public List<ReadTime> getReadTimesByMemberId(Long memberId) {
        return readTimeRepository.findByMemberId(memberId);
    }


}
