package com.project.tms.domain;

import com.project.tms.domain.tag.Tag;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class MemberTag {


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Column(name = "tag_name")
    private String tagName; // Tag 엔티티의 dtype 값을 저장하기 위한 속성
}
