package com.project.tms.tag;

import com.project.tms.domain.tag.Culture;
import com.project.tms.tag.CultureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CultureService {

    private final CultureRepository cultureRepository;

    public void saveCulture(Culture culture) {
        cultureRepository.save(culture);
    }
}
