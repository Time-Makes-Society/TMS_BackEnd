package com.project.tms.tag;

import com.project.tms.domain.tag.Culture;
import com.project.tms.domain.tag.Economy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EconomyService {

    private final EconomyRepository economyRepository;

    public void saveEconomy(Economy economy) {
        economyRepository.save(economy);
    }
}
