package com.project.tms.tag;

import com.project.tms.domain.tag.Politics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliticsRepository extends JpaRepository<Politics, Long> {
}
