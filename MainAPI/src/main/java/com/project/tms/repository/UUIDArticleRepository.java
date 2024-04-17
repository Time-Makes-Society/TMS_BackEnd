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

    @Query("SELECT a FROM UUIDArticle a WHERE a.category = :category")
    List<UUIDArticle> findByCategory(@Param("category") String category);

    default List<UUIDArticle> findClosestToTargetTimeByCategory(String category, LocalTime target, int pageSize, int pageNumber) {
        List<UUIDArticle> articles = findByCategory(category);

        // 결과를 저장할 리스트
        List<UUIDArticle> closestArticles = new ArrayList<>();

        // 가장 target 시간과 유사한 기사들을 선택합니다.
        for (UUIDArticle article : articles) {
            if (Duration.between(article.getArticleTime(), target).abs().getSeconds() <= 300) {
                closestArticles.add(article);
            }
        }

        // 리스트를 페이지별로 잘라서 결과를 반환합니다.
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, closestArticles.size());
        return closestArticles.subList(startIndex, endIndex);
    }
}
