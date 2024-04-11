package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSociety is a Querydsl query type for Society
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSociety extends EntityPathBase<Society> {

    private static final long serialVersionUID = 466742240L;

    public static final QSociety society = new QSociety("society");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QSociety(String variable) {
        super(Society.class, forVariable(variable));
    }

    public QSociety(Path<? extends Society> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSociety(PathMetadata metadata) {
        super(Society.class, metadata);
    }

}

