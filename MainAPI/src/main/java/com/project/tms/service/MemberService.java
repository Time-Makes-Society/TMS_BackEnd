package com.project.tms.service;

import com.project.tms.domain.Member;
import com.project.tms.domain.MemberTag;
import com.project.tms.domain.tag.Tag;
import com.project.tms.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void selectTags(Member member, List<Tag> selectedTags) {
        if (!selectedTags.isEmpty()) {
            member.getTagList().clear();
        }

        for (Tag tag : selectedTags) {
            MemberTag memberTag = new MemberTag();
            memberTag.setMember(member);
            memberTag.setTag(tag);
            member.getTagList().add(memberTag);
        }

        memberRepository.save(member);
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
