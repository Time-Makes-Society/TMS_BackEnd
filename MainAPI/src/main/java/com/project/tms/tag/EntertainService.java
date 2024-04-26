package com.project.tms.tag;

import com.project.tms.domain.tag.Economy;
import com.project.tms.domain.tag.Entertain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntertainService {

    private final EntertainRepository entertainRepository;

    public void saveEntertain(Entertain entertain) {
        entertainRepository.save(entertain);
    }
}
