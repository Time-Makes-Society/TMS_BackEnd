package com.project.tms.tag;

import com.project.tms.domain.tag.Culture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CultureRepository extends JpaRepository<Culture, Long> {
}
