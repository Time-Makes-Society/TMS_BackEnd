package com.project.tms.service;

import com.project.tms.domain.Member;
import com.project.tms.domain.tag.Culture;
import com.project.tms.domain.tag.Entertain;
import com.project.tms.domain.tag.Sports;
import com.project.tms.domain.tag.Tag;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.TagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberService memberService;

    @Autowired private MemberRepository memberRepository;

    @Autowired private TagRepository tagRepository;

    @Test
    void selectTags() {
        //given
        Member member = new Member();
        member.setLoginId("1234");
        member.setMemberName("kim");
        member.setPassWord("1234");

//        // member 저장
//        Long memberId = memberRepository.save(member).getId();

        // member 저장
        Member savedMember = memberRepository.save(member);

        // Culture 생성 및 저장
        Culture culture = new Culture();
        culture.setName("Culture");
        Entertain entertain = new Entertain();
        entertain.setName("Entertain");
        Sports sports = new Sports();
        sports.setName("Sports");
        tagRepository.save(culture);
        tagRepository.save(entertain);
        tagRepository.save(sports);


        // 선택된 태그 ID 목록
        List<Long> selectedTagIds = new ArrayList<>();
        selectedTagIds.add(culture.getId());
        selectedTagIds.add(entertain.getId());
        selectedTagIds.add(sports.getId());

        List<Tag> selectedTags = tagRepository.findByIdIn(selectedTagIds);

        //when
        memberService.selectTags(savedMember, selectedTags);

        //then
        assertEquals(selectedTags.size(), savedMember.getTagList().size());
//        Assertions.assertThat(selectedTags.size()).isEqualTo(3);

    }

}