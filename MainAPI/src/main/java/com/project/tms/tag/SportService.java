package com.project.tms.tag;

import com.project.tms.domain.tag.Sports;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportsRepository sportsRepository;

    public void saveSports(Sports sports) {
        sportsRepository.save(sports);
    }
}
