package com.project.tms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScrapId is a Querydsl query type for ScrapId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QScrapId extends BeanPath<ScrapId> {

    private static final long serialVersionUID = 717932120L;

    public static final QScrapId scrapId = new QScrapId("scrapId");

    public final ComparablePath<java.util.UUID> articleId = createComparable("articleId", java.util.UUID.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QScrapId(String variable) {
        super(ScrapId.class, forVariable(variable));
    }

    public QScrapId(Path<? extends ScrapId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScrapId(PathMetadata metadata) {
        super(ScrapId.class, metadata);
    }

}

