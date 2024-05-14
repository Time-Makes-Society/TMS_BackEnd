package com.project.tms.repository;

import com.project.tms.domain.Article;
import com.project.tms.domain.ArticleLike;
import com.project.tms.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    /*@Query("SELECT a FROM ArticleLike a WHERE a.uuidArticle.id = :articleId AND a.member.id = :memberId")
    ArticleLike findByUuidArticleIdAndMemberId(@Param("articleId") UUID articleId, @Param("memberId") Long memberId);*/

    Optional<ArticleLike> findByUuidArticleIdAndMemberId(UUID uuidArticleId, Long memberId);

}
