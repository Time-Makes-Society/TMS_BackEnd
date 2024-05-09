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

    @Query("SELECT a FROM UUIDArticle a WHERE a.category = :category ORDER BY a.createdDate DESC")
    Page<UUIDArticle> findByCategoryOrderByCreatedDateDesc(@Param("category") String category, Pageable pageable);

    List<UUIDArticle> findByTitle(String title);

    List<UUIDArticle> findByCategoryInOrderByArticleTimeAsc(String[] categories);

    long countByCategoryIn(List<String> categories);
}
