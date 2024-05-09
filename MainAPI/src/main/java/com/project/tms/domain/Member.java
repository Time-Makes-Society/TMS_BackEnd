package com.project.tms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;


    @Column(unique = true, length = 20, nullable = false)
    private String loginId; // 사용자 로그인 ID

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String memberName; // 사용자 이름

    @Column(nullable = false)
    private String memberNickname; // 사용자 닉네임


    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime totalReadTime;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberTag> tagList = new ArrayList<>();


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ReadTime> readTimes = new ArrayList<>();

    public void addReadTime(LocalTime readTime) {
        if (this.totalReadTime == null) {
            this.totalReadTime = readTime; // 최초의 읽기 시간 설정
        } else {
            this.totalReadTime = this.totalReadTime.plusHours(readTime.getHour())
                    .plusMinutes(readTime.getMinute())
                    .plusSeconds(readTime.getSecond()); // 기존 읽기 시간에 새로운 읽기 시간 추가
        }
    }


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ArticleLike> likedArticles = new ArrayList<>();

}
