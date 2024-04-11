package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCulture is a Querydsl query type for Culture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCulture extends EntityPathBase<Culture> {

    private static final long serialVersionUID = -667985178L;

    public static final QCulture culture = new QCulture("culture");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QCulture(String variable) {
        super(Culture.class, forVariable(variable));
    }

    public QCulture(Path<? extends Culture> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCulture(PathMetadata metadata) {
        super(Culture.class, metadata);
    }

}

