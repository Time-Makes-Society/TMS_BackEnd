package com.project.tms.controller;

import com.project.tms.domain.Member;
import com.project.tms.domain.MemberTag;
import com.project.tms.dto.MemberDto;
import com.project.tms.repository.TagRepository;
import com.project.tms.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
//    @PostMapping("/{memberId}/tag")
//    public ResponseEntity<String> selectTags(@PathVariable Long memberId, @RequestBody List<Long> tagIds) {
//        Member member = memberService.findById(memberId).orElse(null);
//        if (member == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        List<Tag> selectedTags = new ArrayList<>();
//        for (Long tagId : tagIds) {
//            Tag tag = tagRepository.findById(tagId).orElse(null);
//            if (tag != null) {
//                selectedTags.add(tag);
//            }
//        }
//
//        memberService.selectTags(member, selectedTags);
//
//
//        return ResponseEntity.ok("Tags selected successfully");
//    }

    /**
     * 파라미터 → String List
     * ex) ["ECONOMY", "ENTERTAIN", "WORLD" ]
     */
    @PostMapping("/{memberId}/tag")
    public ResponseEntity<String> selectTags(@PathVariable Long memberId, @RequestBody List<String> tagNames) {
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        memberService.selectTags(member, tagNames);

        return ResponseEntity.ok("Tags selected successfully");
    }

    @GetMapping("/{memberId}/tags")
    public ResponseEntity<List<String>> getMemberTags(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> tagNames = new ArrayList<>();
        for (MemberTag memberTag : member.getTagList()) {
            Long tagId = memberTag.getTag().getId();
            String tagName = TagIdToName(tagId);
            tagNames.add(tagName);
        }

        return ResponseEntity.ok(tagNames);
    }

    private String TagIdToName(Long tagId) {

        switch (tagId.intValue()) {
            case 1:
                return "CULTURE";
            case 2:
                return "ECONOMY";
            case 3:
                return "ENTERTAIN";
            case 4:
                return "POLITICS";
            case 5:
                return "SCIENCE";
            case 6:
                return "SOCIETY";
            case 7:
                return "SPORTS";
            case 8:
                return "TECHNOLOGY";
            case 9:
                return "WORLD";
            default:
                return null;
        }
    }

    /**
     * 회원 조회
     */
    @GetMapping("{memberId}/get")
    public List<MemberDto> members(@PathVariable Long memberId) {
        Optional<Member> findMember = memberService.findById(memberId);

        return findMember.map(m ->
                        Collections.singletonList(new MemberDto(m.getLoginId(), m.getPassword(), m.getMemberName())))
                .orElse(Collections.emptyList());
    }

}
