package com.project.tms.repository;

import com.project.tms.domain.Member;
import com.project.tms.domain.MemberTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {
}
