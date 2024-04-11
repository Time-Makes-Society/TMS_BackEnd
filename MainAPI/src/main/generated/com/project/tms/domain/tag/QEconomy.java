package com.project.tms.domain.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEconomy is a Querydsl query type for Economy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEconomy extends EntityPathBase<Economy> {

    private static final long serialVersionUID = 594283382L;

    public static final QEconomy economy = new QEconomy("economy");

    public final QTag _super = new QTag(this);

    //inherited
    public final ListPath<com.project.tms.domain.MemberTag, com.project.tms.domain.QMemberTag> category = _super.category;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    public QEconomy(String variable) {
        super(Economy.class, forVariable(variable));
    }

    public QEconomy(Path<? extends Economy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEconomy(PathMetadata metadata) {
        super(Economy.class, metadata);
    }

}

