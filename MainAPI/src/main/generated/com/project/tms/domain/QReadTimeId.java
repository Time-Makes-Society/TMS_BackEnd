package com.project.tms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReadTimeId is a Querydsl query type for ReadTimeId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QReadTimeId extends BeanPath<ReadTimeId> {

    private static final long serialVersionUID = -2111932174L;

    public static final QReadTimeId readTimeId = new QReadTimeId("readTimeId");

    public final ComparablePath<java.util.UUID> articleId = createComparable("articleId", java.util.UUID.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QReadTimeId(String variable) {
        super(ReadTimeId.class, forVariable(variable));
    }

    public QReadTimeId(Path<? extends ReadTimeId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReadTimeId(PathMetadata metadata) {
        super(ReadTimeId.class, metadata);
    }

}

