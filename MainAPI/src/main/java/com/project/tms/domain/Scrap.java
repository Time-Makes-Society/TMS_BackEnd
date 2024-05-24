package com.project.tms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {

    public Scrap(Member member, UUIDArticle uuidArticle) {
        this.member = member;
        this.uuidArticle = uuidArticle;
        this.title = uuidArticle.getTitle();
        this.content = uuidArticle.getContent();
        this.createdDate = uuidArticle.getCreatedDate();
        this.category = uuidArticle.getCategory();
        this.image = uuidArticle.getImage();
        this.link = uuidArticle.getLink();
        this.articleTime = uuidArticle.getArticleTime();
        this.publisher = uuidArticle.getPublisher();
    }

    @Id
    @GeneratedValue
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
    private LocalDateTime createdDate;
    private String category;
    private String image;
    private String link;
    private LocalTime articleTime;

    private String publisher;
}
