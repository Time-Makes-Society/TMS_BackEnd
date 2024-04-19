package com.project.tms.tag;

import com.project.tms.domain.tag.Science;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScienceService {

    private final ScienceRepository scienceRepository;

    public void saveScience(Science science) {
        scienceRepository.save(science);
    }
}
