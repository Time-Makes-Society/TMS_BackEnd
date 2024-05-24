package com.project.tms.domain.tag;

import com.project.tms.domain.MemberTag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Tag {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;


    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<MemberTag> category = new ArrayList<>();

}
