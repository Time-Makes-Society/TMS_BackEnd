package com.project.tms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReadTime is a Querydsl query type for ReadTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReadTime extends EntityPathBase<ReadTime> {

    private static final long serialVersionUID = 328528247L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReadTime readTime1 = new QReadTime("readTime1");

    public final QArticle article;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final DateTimePath<java.time.LocalDateTime> readTime = createDateTime("readTime", java.time.LocalDateTime.class);

    public QReadTime(String variable) {
        this(ReadTime.class, forVariable(variable), INITS);
    }

    public QReadTime(Path<? extends ReadTime> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReadTime(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReadTime(PathMetadata metadata, PathInits inits) {
        this(ReadTime.class, metadata, inits);
    }

    public QReadTime(Class<? extends ReadTime> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new QArticle(forProperty("article")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

