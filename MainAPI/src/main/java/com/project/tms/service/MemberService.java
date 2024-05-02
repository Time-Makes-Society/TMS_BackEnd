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

import java.time.Duration;
import java.time.LocalTime;
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
        member.setMemberNickname(memberDto.getMemberNickname());

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


//    @Transactional
//    public void selectTags(Member member, List<String> tagNames) {
////        if (!tagNames.isEmpty()) {
////            member.getTagList().clear();
////        }
//        member.getTagList().clear();
//
//        for (String tagName : tagNames) {
//
//            Long tagId = findTagIdByName(tagName);
//
//            if (tagId != null) {
//
//                Tag tag = tagRepository.findById(tagId).orElse(null);
//
//                if (tag != null) {
//                    MemberTag memberTag = new MemberTag();
//                    memberTag.setMember(member);
//                    memberTag.setTag(tag);
//                    member.getTagList().add(memberTag);
//                }
//            }
//        }
//        memberRepository.save(member);
//    }

    @Transactional
    public void selectTags(Member member, List<String> tagNames) {
        // 새로운 태그 리스트를 추가합니다.
        for (String tagName : tagNames) {
            Long tagId = findTagIdByName(tagName);
            if (tagId != null) {
                // 이미 추가된 태그는 다시 추가하지 않도록 중복을 확인합니다.
                boolean tagExists = member.getTagList().stream()
                        .anyMatch(memberTag -> memberTag.getTag().getId().equals(tagId));
                if (!tagExists) {
                    Tag tag = tagRepository.findById(tagId).orElse(null);
                    if (tag != null) {
                        MemberTag memberTag = new MemberTag();
                        memberTag.setMember(member);
                        memberTag.setTag(tag);
                        member.getTagList().add(memberTag);
                    }
                }
            }
        }
        // 새로운 태그 리스트만을 저장합니다.
        memberRepository.save(member);
    }


    private Long findTagIdByName(String tagName) {
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

    /**
     * 기사 읽은 시간 누적
     */
    @Transactional
    public void addReadTimeToMember(Long memberId, LocalTime readTime) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다."));
        member.addReadTime(readTime);
        memberRepository.save(member);
    }
    
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
