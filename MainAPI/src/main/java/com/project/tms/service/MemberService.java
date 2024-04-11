package com.project.tms.service;

import com.project.tms.domain.Member;
import com.project.tms.domain.MemberTag;
import com.project.tms.domain.tag.Tag;
import com.project.tms.dto.MemberDto;
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

    /**
     * 회원가입
     */
    @Transactional
    public Long join(MemberDto memberDto) {
        validateDuplicateMember(memberDto);

        Member member = new Member();
        member.setLoginId(memberDto.getLoginId());
        member.setPassword(memberDto.getPassword());
        member.setMemberName(memberDto.getMemberName());

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        List<Member> findMembers = memberRepository.findByLoginId(memberDto.getLoginId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 태그 선택
     */
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
