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

    private String title;
    @Lob
    @Column(columnDefinition = "text")
    private String content;
    private String createdDate;
    private String category;
    private String image;
    private String link;

    private Scrap() {
    }

    public Scrap(Member member, UUIDArticle uuidArticle) {
        this.member = member;
        this.uuidArticle = uuidArticle;
        this.title = uuidArticle.getTitle();
        this.content = uuidArticle.getContent();
        this.createdDate = uuidArticle.getCreatedDate();
        this.category = uuidArticle.getCategory();
        this.image = uuidArticle.getImage();
        this.link = uuidArticle.getLink();
    }
}
