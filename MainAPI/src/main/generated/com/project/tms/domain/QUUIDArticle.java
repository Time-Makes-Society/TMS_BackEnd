package com.project.tms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUUIDArticle is a Querydsl query type for UUIDArticle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUUIDArticle extends EntityPathBase<UUIDArticle> {

    private static final long serialVersionUID = 2078390375L;

    public static final QUUIDArticle uUIDArticle = new QUUIDArticle("uUIDArticle");

    public final StringPath category = createString("category");

    public final StringPath content = createString("content");

    public final StringPath createdDate = createString("createdDate");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath image = createString("image");

    public final StringPath link = createString("link");

    public final StringPath title = createString("title");

    public QUUIDArticle(String variable) {
        super(UUIDArticle.class, forVariable(variable));
    }

    public QUUIDArticle(Path<? extends UUIDArticle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUUIDArticle(PathMetadata metadata) {
        super(UUIDArticle.class, metadata);
    }

}

