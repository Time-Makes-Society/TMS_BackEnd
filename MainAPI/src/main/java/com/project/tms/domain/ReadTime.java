package com.project.tms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class ReadTime {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "uuid_id")
//    private UUIDArticle uuidArticle;

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


    public LocalTime getCategoryValue() {
        switch (this.category) {
            case "문화":
                return this.culture;
            case "경제":
                return this.economy;
            case "연예":
                return this.entertain;
            case "정치":
                return this.politics;
            case "과학":
                return this.science;
            case "사회":
                return this.society;
            case "스포츠":
                return this.sports;
            case "기술":
                return this.technology;
            case "해외":
                return this.world;
            default:
                return null; // 기본적으로 null을 반환하거나 다른 동작을 설정합니다.
        }
    }
}
