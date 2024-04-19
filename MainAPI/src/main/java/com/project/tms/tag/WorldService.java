package com.project.tms.tag;

import com.project.tms.domain.tag.World;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorldService {

    private final WorldRepository worldRepository;

    public void saveWorld(World world) {
        worldRepository.save(world);
    }
}
