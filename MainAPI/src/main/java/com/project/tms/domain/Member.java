package com.project.tms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

//    @NonNull
    @Column(unique = true, length=10)
    private String memberName;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime totalReadTime;
}
