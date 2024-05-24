package com.project.tms.repository;

import com.project.tms.domain.Member;
import com.project.tms.domain.Scrap;
import com.project.tms.domain.UUIDArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    boolean existsByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);

    Optional<Scrap> findByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);


    @Query("SELECT s.title, s.category, s.image FROM Scrap s WHERE s.member.id = :memberId")
    List<Object[]> findTitleAndCategoryByMemberId(@Param("memberId") Long memberId);

    List<Scrap> findScrapByMemberId(@Param("memberId") Long memberId);

}
