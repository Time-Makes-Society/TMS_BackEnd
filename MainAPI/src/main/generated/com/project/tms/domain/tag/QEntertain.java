package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEntertain is a Querydsl query type for Entertain
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEntertain extends EntityPathBase<Entertain> {

    private static final long serialVersionUID = 1754572386L;

    public static final QEntertain entertain = new QEntertain("entertain");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QEntertain(String variable) {
        super(Entertain.class, forVariable(variable));
    }

    public QEntertain(Path<? extends Entertain> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEntertain(PathMetadata metadata) {
        super(Entertain.class, metadata);
    }

}

