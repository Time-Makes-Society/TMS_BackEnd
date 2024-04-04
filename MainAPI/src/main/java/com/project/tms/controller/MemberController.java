package com.project.tms.controller;

import com.project.tms.domain.Member;
import com.project.tms.domain.tag.Tag;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.TagRepository;
import com.project.tms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TagRepository tagRepository;

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
