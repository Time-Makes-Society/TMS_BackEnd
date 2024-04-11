package com.project.tms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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

    private String createdDate;
    private String category;
    private String image;
    private String link;
    private String articleTime;
}
