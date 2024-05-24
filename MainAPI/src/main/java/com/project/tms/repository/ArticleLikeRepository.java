package com.project.tms.repository;


import com.project.tms.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {


    Optional<ArticleLike> findByUuidArticleIdAndMemberId(UUID uuidArticleId, Long memberId);

}
