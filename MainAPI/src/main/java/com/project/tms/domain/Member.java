package com.project.tms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NonNull
//    @Column(unique = true, length=10)
    @Column(unique = true)
    private String memberName;

    private String passWord;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime totalReadTime;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberTag> tagList = new ArrayList<>();

    // getTagList 메서드
    public List<MemberTag> getTagList() {
        return this.tagList;
    }
}
