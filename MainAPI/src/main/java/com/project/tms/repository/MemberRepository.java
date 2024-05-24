package com.project.tms.repository;

import com.project.tms.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByLoginId(String loginId);

    Optional<Member> findAnyByLoginId(String loginId);

}
