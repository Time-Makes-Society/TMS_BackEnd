package com.project.tms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ArticleLike", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"uuid_id", "member_id"})
})
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid_id")
    private UUIDArticle uuidArticle;

    @Column(nullable = false)
    private boolean liked; // 좋아요 여부를 나타내는 boolean 필드
}
