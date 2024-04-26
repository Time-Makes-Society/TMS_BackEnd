package com.project.tms.tag;

import com.project.tms.domain.tag.Technology;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public void saveTechnology(Technology technology) {
        technologyRepository.save(technology);
    }
}
