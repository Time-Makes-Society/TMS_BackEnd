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
    void deleteByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);

    boolean existsByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);

    Optional<Scrap> findByMemberAndUuidArticle(Member member, UUIDArticle uuidArticle);

//    @Query("SELECT s.title, s.category, s.content FROM Scrap s WHERE s.member.id = :memberId")
//    @Query("SELECT s.title FROM Scrap s WHERE s.member.id = :memberId")
//    List<String> findTitleByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT s.title FROM Scrap s WHERE s.member.id = :memberId")
    List<String> findTitleByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT s.category From Scrap s WHERE s.member.id = :memberId")
    List<String> findCategoryByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT s.title, s.category FROM Scrap s WHERE s.member.id = :memberId")
    List<Object[]> findTitleAndCategoryByMemberId(@Param("memberId") Long memberId);

}
