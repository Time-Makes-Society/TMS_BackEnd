package com.project.tms.tag;

import com.project.tms.domain.tag.Politics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PoliticsService {

    private final PoliticsRepository politicsRepository;

    public void savePolitics(Politics politics) {
        politicsRepository.save(politics);
    }
}
