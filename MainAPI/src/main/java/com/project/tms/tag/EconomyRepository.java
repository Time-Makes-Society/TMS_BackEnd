package com.project.tms.tag;

import com.project.tms.domain.tag.Economy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EconomyRepository extends JpaRepository<Economy, Long> {
}
