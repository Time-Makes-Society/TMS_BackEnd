package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTechnology is a Querydsl query type for Technology
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTechnology extends EntityPathBase<Technology> {

    private static final long serialVersionUID = 1972360404L;

    public static final QTechnology technology = new QTechnology("technology");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QTechnology(String variable) {
        super(Technology.class, forVariable(variable));
    }

    public QTechnology(Path<? extends Technology> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTechnology(PathMetadata metadata) {
        super(Technology.class, metadata);
    }

}

