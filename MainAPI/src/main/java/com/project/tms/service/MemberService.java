package com.project.tms.service;

import com.project.tms.domain.Member;
import com.project.tms.domain.MemberTag;
import com.project.tms.domain.tag.Tag;
import com.project.tms.dto.MemberDto;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.TagRepository;
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
    private final TagRepository tagRepository;

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
//    @Transactional
//    public void selectTags(Member member, List<Tag> selectedTags) {
//        if (!selectedTags.isEmpty()) {
//            member.getTagList().clear();
//        }
//
//        for (Tag tag : selectedTags) {
//            MemberTag memberTag = new MemberTag();
//            memberTag.setMember(member);
//            memberTag.setTag(tag);
//            member.getTagList().add(memberTag);
//        }
//        memberRepository.save(member);
//    }
    @Transactional
    public void selectTags(Member member, List<String> tagNames) {
        if (!tagNames.isEmpty()) {
            member.getTagList().clear();
        }

        for (String tagName : tagNames) {
            // 태그 이름을 사용하여 해당하는 ID를 찾아옵니다.
            Long tagId = findTagIdByName(tagName);
            if (tagId != null) {
                // 찾은 태그의 ID를 사용하여 MemberTag를 생성하고 저장합니다.
                Tag tag = tagRepository.findById(tagId).orElse(null);
                if (tag != null) {
                    MemberTag memberTag = new MemberTag();
                    memberTag.setMember(member);
                    memberTag.setTag(tag);
                    member.getTagList().add(memberTag);
                }
            }
        }
        memberRepository.save(member);
    }

    private Long findTagIdByName(String tagName) {
        // 실제로는 태그 이름을 사용하여 ID를 찾는 방법을 구현해야 합니다.
        // 여기에서는 가정으로 작성된 코드입니다.
        switch (tagName) {
            case "CULTURE":
                return 1L;
            case "ECONOMY":
                return 2L;
            case "ENTERTAIN":
                return 3L;
            case "POLITICS":
                return 4L;
            case "SCIENCE":
                return 5L;
            case "SOCIETY":
                return 6L;
            case "SPORTS":
                return 7L;
            case "TECHNOLOGY":
                return 8L;
            case "WORLD":
                return 9L;
            default:
                return null;
        }
    }


    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
