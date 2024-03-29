package com.project.tms.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Tag {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    private String name;
}
