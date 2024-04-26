package com.project.tms.tag;

import com.project.tms.domain.tag.Science;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScienceRepository extends JpaRepository<Science, Long> {
}
