package com.project.tms.repository;

import com.project.tms.domain.ReadTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadTimeRepository extends JpaRepository<ReadTime, Long> {
    ReadTime findByMemberIdAndCategory(Long memberId, String category);
    ReadTime findByMemberId(Long memberId);
}
