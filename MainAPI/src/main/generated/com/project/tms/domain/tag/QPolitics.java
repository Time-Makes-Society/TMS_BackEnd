package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPolitics is a Querydsl query type for Politics
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPolitics extends EntityPathBase<Politics> {

    private static final long serialVersionUID = 908741929L;

    public static final QPolitics politics = new QPolitics("politics");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QPolitics(String variable) {
        super(Politics.class, forVariable(variable));
    }

    public QPolitics(Path<? extends Politics> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPolitics(PathMetadata metadata) {
        super(Politics.class, metadata);
    }

}

