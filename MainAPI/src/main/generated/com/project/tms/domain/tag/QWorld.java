package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorld is a Querydsl query type for World
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorld extends EntityPathBase<World> {

    private static final long serialVersionUID = 1376259786L;

    public static final QWorld world = new QWorld("world");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QWorld(String variable) {
        super(World.class, forVariable(variable));
    }

    public QWorld(Path<? extends World> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorld(PathMetadata metadata) {
        super(World.class, metadata);
    }

}

