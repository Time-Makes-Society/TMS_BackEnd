package com.project.tms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "news")
public class UUIDArticle {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "uuid_id", columnDefinition = "BINARY(16)")
    private UUID id;

    private String title;

    @Lob
    @Column(columnDefinition = "text")
    private String content; // 원문 내용

    @Column(columnDefinition = "text")
    private String gptContent; // 요약한 내용

    private String category;

    private String image;

    private String link;

    private String publisher;

    private LocalTime articleTime;

    private LocalDateTime createdDate;

    @Column(columnDefinition = "text")
    private String embedding; // 추천서비스를 위한 타이틀 embedding 값을 저장 필드

    @Column(columnDefinition = "text")
    private String contentEmbedding; // 요약하기 전의 내용 embedding 값

    @Column(columnDefinition = "text")
    private String gptContentEmbedding; // 요약한 후의 embedding 값

    private Long diffContentEmbedding;// 요약하기 전과 요약한 후의 임베딩 차이

    @Column(nullable = false)
    private Long likeCount = 0L; // 좋아요 수 필드 추가 및 초기값 설정

    @OneToMany(mappedBy = "uuidArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleLike> likedByMembers = new ArrayList<>();

    public void addLike(ArticleLike like) {
        likedByMembers.add(like);
        like.setUuidArticle(this);
    }

    public void removeLike(ArticleLike like) {
        likedByMembers.remove(like);
        like.setUuidArticle(null);
    }

}
