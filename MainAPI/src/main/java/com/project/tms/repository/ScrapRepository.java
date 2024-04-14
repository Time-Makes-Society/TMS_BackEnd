package com.project.tms.repository;

import com.project.tms.domain.Article;
import com.project.tms.domain.Member;
import com.project.tms.domain.Scrap;
import com.project.tms.domain.UUIDArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    void deleteByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);

    boolean existsByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);

    Optional<Scrap> findByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);
}
