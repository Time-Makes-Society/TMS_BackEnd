package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScience is a Querydsl query type for Science
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScience extends EntityPathBase<Science> {

    private static final long serialVersionUID = 128622492L;

    public static final QScience science = new QScience("science");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QScience(String variable) {
        super(Science.class, forVariable(variable));
    }

    public QScience(Path<? extends Science> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScience(PathMetadata metadata) {
        super(Science.class, metadata);
    }

}

