package com.project.tms.repository;

import com.project.tms.domain.Comment;
import com.project.tms.domain.UUIDArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.article = :article")
    List<Comment> findByArticle(@Param("article") UUIDArticle article);

    @Query("SELECT c FROM Comment c WHERE c.id = :commentId AND c.article.id = :articleId")
    Optional<Comment> findByIdAndArticleId(@Param("commentId") Long commentId, @Param("articleId") UUIDArticle articleId);

    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND c.article.id = :articleId")
    void deleteByIdAndArticleId(@Param("commentId") Long commentId, @Param("articleId") UUIDArticle articleId);
}

