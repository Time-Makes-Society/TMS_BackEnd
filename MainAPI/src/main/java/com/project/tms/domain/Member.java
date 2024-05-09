package com.project.tms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
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

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime totalReadTime;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberTag> tagList = new ArrayList<>();

    /*@ManyToMany
    @JoinTable(
            name = "ArticleLike",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "uuid_id")
    )
    private List<UUIDArticle> likedArticles = new ArrayList<>();*/
    @OneToMany(mappedBy = "member")
    private List<ArticleLike> likedArticles = new ArrayList<>();

}
