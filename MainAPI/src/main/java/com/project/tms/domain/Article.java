package com.project.tms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pre_news")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // ID 값은 직접 설정


    private String title;
    @Lob
    @Column(columnDefinition = "text")
    private String content;
    private String createdDate;
    private String category;
    private String image;
    private String link;
}

