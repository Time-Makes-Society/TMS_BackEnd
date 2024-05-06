package com.project.tms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class ReadTime {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "category")
    private String category;

    @Column(name = "read_time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime readTime;

    private LocalTime culture;
    private LocalTime economy;
    private LocalTime entertain;
    private LocalTime politics;
    private LocalTime science;
    private LocalTime society;
    private LocalTime sports;
    private LocalTime technology;
    private LocalTime world;

}
