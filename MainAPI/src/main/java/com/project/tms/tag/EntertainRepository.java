package com.project.tms.tag;

import com.project.tms.domain.tag.Culture;
import com.project.tms.domain.tag.Entertain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntertainRepository extends JpaRepository<Entertain, Long> {
}
