package com.project.tms.tag;

import com.project.tms.domain.tag.Society;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocietyService {

    private final SocietyRepository societyRepository;

    public void saveSociety(Society society) {
        societyRepository.save(society);
    }
}
