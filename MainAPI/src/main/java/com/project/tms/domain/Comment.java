package com.project.tms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content; // 댓글 내용

    @JsonFormat(pattern = "yyyyMMdd HH:mm:ss")
    private LocalDateTime commentCreatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private UUIDArticle article; // 댓글이 속한 기사

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Member user; // 댓글 작성자


    @PrePersist
    protected void onCreate() {
        this.commentCreatedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
