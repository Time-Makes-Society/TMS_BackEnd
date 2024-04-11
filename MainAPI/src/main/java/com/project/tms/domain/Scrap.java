package com.project.tms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static jakarta.persistence.FetchType.*;

@Entity
@Table
@Getter @Setter
public class Scrap {

//    @EmbeddedId
//    private ScrapId id;

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "uuid_id")
    private UUIDArticle uuidArticle;
}
