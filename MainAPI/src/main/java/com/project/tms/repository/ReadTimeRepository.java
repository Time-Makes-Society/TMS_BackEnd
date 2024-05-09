package com.project.tms.repository;

import com.project.tms.domain.ReadTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadTimeRepository extends JpaRepository<ReadTime, Long>, CrudRepository<ReadTime, Long> {
    ReadTime findByMemberIdAndCategory(Long memberId, String category);
//    ReadTime findByMemberId(Long memberId);
//    @Query("SELECT rt FROM ReadTime rt JOIN rt.member m WHERE m.id = :memberId")
//    ReadTime findByMemberId(Long memberId);

    List<ReadTime> findByMemberId(Long memberId);

}
