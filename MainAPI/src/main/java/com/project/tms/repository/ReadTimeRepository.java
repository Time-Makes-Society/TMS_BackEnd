package com.project.tms.repository;

import com.project.tms.domain.ReadTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadTimeRepository extends JpaRepository<ReadTime, Long>, CrudRepository<ReadTime, Long> {
    ReadTime findByMemberIdAndCategory(Long memberId, String category);

    List<ReadTime> findByMemberId(Long memberId);

}
