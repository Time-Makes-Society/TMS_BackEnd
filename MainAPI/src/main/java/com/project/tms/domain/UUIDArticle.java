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
    private String content;

    private String category;

    private String image;

    private String link;

    private String publisher;

    private LocalTime articleTime;

    private LocalDateTime createdDate;

    @Column(columnDefinition = "text")
    private String embedding; // 추천서비스를 위한 embedding 값을 이용함


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
