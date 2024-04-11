package com.project.tms.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ScrapId implements Serializable {

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @Column(name = "article_id", insertable = false, updatable = false)
    private UUID articleId;
}
