package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSports is a Querydsl query type for Sports
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSports extends EntityPathBase<Sports> {

    private static final long serialVersionUID = -399295673L;

    public static final QSports sports = new QSports("sports");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QSports(String variable) {
        super(Sports.class, forVariable(variable));
    }

    public QSports(Path<? extends Sports> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSports(PathMetadata metadata) {
        super(Sports.class, metadata);
    }

}

