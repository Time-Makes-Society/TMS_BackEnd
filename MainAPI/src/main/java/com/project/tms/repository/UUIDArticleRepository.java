package com.project.tms.repository;

import com.project.tms.domain.UUIDArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UUIDArticleRepository extends JpaRepository<UUIDArticle, UUID> {

    // SQL 쿼리를 사용하여 직접 메서드 정의
//    @Query("SELECT a FROM UUIDArticle a WHERE a.category IN :categories ORDER BY a.createdDate DESC")
//    Page<UUIDArticle> findByCategoryInOrderByCreatedDateDesc(List<String> categories, Pageable pageable);
    @Query("SELECT a FROM UUIDArticle a WHERE a.category IN :categories ORDER BY a.createdDate DESC")
    Page<UUIDArticle> findByCategoryInOrderByCreatedDateDesc(@Param("categories") List<String> categories, Pageable pageable);
}

/*
package com.project.tms.repository;

import com.project.tms.domain.UUIDArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface UUIDArticleRepository extends JpaRepository<UUIDArticle, UUID>, QuerydslPredicateExecutor<UUIDArticle> {
    Page<UUIDArticle> findByCategoryInAndCreatedDateAfterOrderByCreatedDateDesc(List<String> categories, DateTime date, Pageable pageable);
}
*/
