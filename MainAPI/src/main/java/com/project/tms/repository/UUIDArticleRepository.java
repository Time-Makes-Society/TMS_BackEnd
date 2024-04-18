package com.project.tms.repository;

import com.project.tms.domain.UUIDArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface UUIDArticleRepository extends JpaRepository<UUIDArticle, UUID> {

    @Query("SELECT a FROM UUIDArticle a WHERE a.category = :category ORDER BY a.createdDate DESC")
    Page<UUIDArticle> findByCategoryOrderByCreatedDateDesc(@Param("category") String category, Pageable pageable);

    // 해당 카테고리의 target 쿼리스트링 시간보다 작은 데이터를 검색하는 메서드
    @Query("SELECT a FROM UUIDArticle a WHERE a.category = :category AND a.articleTime < :targetTime ORDER BY a.createdDate DESC")
    List<UUIDArticle> findByCategoryAndArticleTimeBefore(@Param("category") String category, @Param("targetTime") LocalTime targetTime);

    // 해당 카테고리의 모든 기사를 검색하는 메서드
    @Query("SELECT a FROM UUIDArticle a WHERE a.category = :category")
    List<UUIDArticle> findByCategory(@Param("category") String category);

    List<UUIDArticle> findByTitle(String title);

    @Query(value = "SELECT a.* " +
            "FROM ( " +
            "    SELECT *, ABS(TIME_TO_SEC(TIMEDIFF(articleTime, :targetTime))) AS time_diff " +
            "    FROM news " +
            "    WHERE category IN :categories " +
            ") AS a " +
            "ORDER BY time_diff ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<UUIDArticle> findClosestArticlesByCategoriesAndTargetTime(@Param("categories") List<String> categories, @Param("targetTime") LocalTime targetTime, @Param("limit") int limit);
}
