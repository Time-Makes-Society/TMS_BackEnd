package com.project.tms.repository;

import com.project.tms.domain.UUIDArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UUIDArticleRepository extends JpaRepository<UUIDArticle, Long> {
}
