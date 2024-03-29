package com.project.tms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class MemberTagId implements Serializable {

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @Column(name = "tag_id", insertable = false, updatable = false)
    private Long tagId;
}
