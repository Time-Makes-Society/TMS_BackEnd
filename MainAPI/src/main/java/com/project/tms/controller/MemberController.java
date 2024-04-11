package com.project.tms.controller;

import com.project.tms.domain.Member;
import com.project.tms.domain.tag.Tag;
import com.project.tms.dto.MemberDto;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.TagRepository;
import com.project.tms.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TagRepository tagRepository;

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberDto memberDto) {
        log.info("회원가입 실행");
        Long memberId = memberService.join(memberDto);
//        return ResponseEntity.ok("회원가입이 완료되었습니다. 회원 ID: " + memberId);
        return ResponseEntity.ok("회원가입이 완료되었습니다. 회원 loginId: " + memberDto.getLoginId());
    }


    /**
     * 태그 선택
     */
    @PostMapping("/{memberId}/tag")
    public ResponseEntity<String> selectTags(@PathVariable Long memberId, @RequestBody List<Long> tagIds) {
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        List<Tag> selectedTags = new ArrayList<>();
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId).orElse(null);
            if (tag != null) {
                selectedTags.add(tag);
            }
        }

        memberService.selectTags(member, selectedTags);


        return ResponseEntity.ok("Tags selected successfully");
    }
}
